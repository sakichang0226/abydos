package com.project.abydos.saki.api.orders.service;

import com.project.abydos.saki.api.orders.constant.OrderErrorLogMessage;
import com.project.abydos.saki.api.orders.exception.OutOfStockException;
import com.project.abydos.saki.api.orders.exception.ProductNotFoundException;
import com.project.abydos.saki.api.orders.exception.ProductUnavailableException;
import com.project.abydos.saki.api.orders.request.OrderConfirmedRequest;
import com.project.abydos.saki.api.orders.response.OrdersApiResponse;
import com.project.abydos.saki.api.orders.constant.DeliveryStatus;
import com.project.abydos.saki.common.constant.ProductStatus;
import com.project.abydos.saki.dynamodb.entity.Order;
import com.project.abydos.saki.dynamodb.entity.OrderDetail;
import com.project.abydos.saki.dynamodb.entity.Product;
import com.project.abydos.saki.dynamodb.repository.*;
import com.project.abydos.saki.dynamodb.exception.StockConditionException;
import com.project.abydos.saki.dynamodb.param.OrderTransactionParam;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 注文履歴一覧API サービス.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrdersService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;
    private final SequenceRepository sequenceRepository;

    /**
     * 注文履歴一覧を取得する.
     * ordersテーブルからQueryで取得後、detail_idsを展開してorder_detailsテーブルからBatchGetItemで一括取得する.
     *
     * @param seqUserId ユーザーID
     * @param limit 取得件数
     * @param lastOrderId ページネーション用の最後に取得したOrderId
     * @return 注文履歴一覧レスポンス
     */
    public OrdersApiResponse getOrders(@NonNull Long seqUserId, int limit, Long lastOrderId) {

        PagedResult<Order> result = orderRepository.findByUserId(seqUserId, limit, lastOrderId);

        if (result.getItems().isEmpty()) {
            return OrdersApiResponse.builder().orders(Collections.emptyList()).build();
        }

        List<Order> orders = result.getItems();

        // detail_idsを展開し、BatchGetItemで一括取得
        Map<Long, Set<Long>> orderDetailIdsMap = orders.stream()
                .filter(o -> !CollectionUtils.isEmpty(o.getDetailIds()))
                .collect(Collectors.toMap(Order::getOrderId, Order::getDetailIds));

        Map<Long, List<OrderDetail>> detailsByOrderId = Collections.emptyMap();
        if (!orderDetailIdsMap.isEmpty()) {
            List<OrderDetail> details = orderDetailRepository.batchGetByOrderDetailIds(orderDetailIdsMap);
            detailsByOrderId = details.stream()
                    .collect(Collectors.groupingBy(OrderDetail::getOrderId));
        }

        // レスポンスマッピング
        Map<Long, List<OrderDetail>> finalDetailsByOrderId = detailsByOrderId;
        List<OrdersApiResponse.OrderDetail> orderDetails = orders.stream()
                .map(order -> mapToOrderDetail(order, finalDetailsByOrderId.getOrDefault(order.getOrderId(), Collections.emptyList())))
                .toList();

        return OrdersApiResponse.builder()
                .lastOrderId(result.getLastEvaluatedSortKey())
                .orders(orderDetails)
                .build();
    }

    /**
     * Orderと対応するOrderDetailリストから注文詳細レスポンスを生成する.
     *
     * @param order 注文エンティティ
     * @param details 受注明細エンティティリスト
     * @return 注文詳細レスポンス
     */
    private OrdersApiResponse.OrderDetail mapToOrderDetail(Order order, List<OrderDetail> details) {
        List<OrdersApiResponse.DetailResponse> detailResponses = details.stream()
                .map(this::mapToDetailResponse)
                .toList();

        long total = details.stream()
                .mapToLong(d -> d.getPrice() * d.getOrderNum())
                .sum();

        String deliveryStatus = details.isEmpty()
                ? DeliveryStatus.PROCESSING.getCode() : (
                        details.stream().allMatch(d -> DeliveryStatus.DELIVERED.getCode().equals(d.getDeliveryStatus()))
                   ? DeliveryStatus.DELIVERED.getCode() : DeliveryStatus.PROCESSING.getCode());

        return OrdersApiResponse.OrderDetail.builder()
                .orderId(order.getOrderId())
                .createdAt(order.getCreatedAt())
                .total(total)
                .details(detailResponses)
                .deliveryStatus(deliveryStatus)
                .build();
    }

    /**
     * OrderDetailエンティティから受注詳細レスポンスを生成する.
     *
     * @param detail 受注明細エンティティ
     * @return 受注詳細レスポンス
     */
    private OrdersApiResponse.DetailResponse mapToDetailResponse(OrderDetail detail) {
        return OrdersApiResponse.DetailResponse.builder()
                .detailId(detail.getDetailId())
                .productId(detail.getProductId())
                .productName(detail.getProductName())
                .shopId(detail.getShopId())
                .price(detail.getPrice())
                .orderNum(detail.getOrderNum())
                .total(detail.getPrice() * detail.getOrderNum())
                .build();
    }

    /**
     * 注文確定処理.
     * リクエストの注文可否チェックを実施し、TransactWriteItemsで注文を登録する.
     *
     * @param seqUserId ユーザーID
     * @param purchaseProducts 注文商品リスト
     */
    public void confirmed(@NonNull Long seqUserId, List<OrderConfirmedRequest.Product> purchaseProducts) {

        // 3.1 同一商品の数量を集約
        Map<Long, Long> aggregatedQuantities = purchaseProducts.stream()
                .collect(Collectors.groupingBy(
                        OrderConfirmedRequest.Product::getProduct_id,
                        Collectors.summingLong(OrderConfirmedRequest.Product::getQuantity)));

        // 3.2 productsテーブルから集約後のproduct_idをキーにしてBatchGetItemで取得
        List<Long> productIds = new ArrayList<>(aggregatedQuantities.keySet());
        List<Product> products = productRepository.findByIds(productIds);

        // 3.3 商品が存在しない場合
        if (products.size() != productIds.size()) {
            Set<Long> foundIds = products.stream().map(Product::getProductId).collect(Collectors.toSet());
            List<Long> missingIds = productIds.stream().filter(id -> !foundIds.contains(id)).toList();
            throw new ProductNotFoundException(OrderErrorLogMessage.PRODUCT_NOT_FOUND.formatDetail(missingIds));
        }

        Map<Long, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getProductId, p -> p));

        for (Map.Entry<Long, Long> entry : aggregatedQuantities.entrySet()) {
            Product product = productMap.get(entry.getKey());

            // 3.4 商品ステータスが購入不可
            if (!ProductStatus.ON_SALE.getCode().equals(product.getStatus())) {
                throw new ProductUnavailableException(OrderErrorLogMessage.PRODUCT_UNAVAILABLE.formatDetail(product.getProductId()));
            }

            // 3.5 在庫数 < 集約後の注文数量
            if (product.getStock() < entry.getValue()) {
                throw new OutOfStockException(OrderErrorLogMessage.OUT_OF_STOCK.formatDetail(product.getProductId(), product.getStock(), entry.getValue()));
            }
        }

        // 4. 注文履歴テーブルの登録（TransactWriteItems）

        // 4.1 order_id採番
        Long orderId = sequenceRepository.getNextValue("order_id", 1L);

        // 4.2 detail_id採番（件数分まとめてインクリメント）
        int detailCount = aggregatedQuantities.size();
        Long lastDetailId = sequenceRepository.getNextValue("detail_id", (long) detailCount);
        Long firstDetailId = lastDetailId - detailCount + 1;

        // 明細パラメータ構築
        List<OrderTransactionParam.DetailParam> detailParams = aggregatedQuantities.entrySet().stream()
                .map(entry -> {
                    Product product = productMap.get(entry.getKey());
                    return OrderTransactionParam.DetailParam.builder()
                            .productId(entry.getKey())
                            .productName(product.getProductName())
                            .shopId(product.getShopId())
                            .price(product.getPrice())
                            .quantity(entry.getValue())
                            .build();
                }).toList();

        // トランザクション実行
        OrderTransactionParam param = OrderTransactionParam.builder()
                .userId(seqUserId)
                .orderId(orderId)
                .firstDetailId(firstDetailId)
                .createdAt(System.currentTimeMillis())
                .initialDeliveryStatus(DeliveryStatus.PROCESSING.getCode())
                .details(detailParams)
                .build();

        try {
            orderRepository.saveOrder(param);
        } catch (StockConditionException ex) {
            throw new OutOfStockException(OrderErrorLogMessage.STOCK_CONDITION_CONFLICT.getDetailFormat());
        }
    }
}
