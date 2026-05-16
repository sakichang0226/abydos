package com.project.abydos.saki.api.orders.service;

import com.project.abydos.saki.api.orders.response.OrdersApiResponse;
import com.project.abydos.saki.api.orders.constant.DeliveryStatus;
import com.project.abydos.saki.dynamodb.entity.Order;
import com.project.abydos.saki.dynamodb.entity.OrderDetail;
import com.project.abydos.saki.dynamodb.repository.OrderRepository;
import com.project.abydos.saki.dynamodb.repository.PagedResult;
import com.project.abydos.saki.dynamodb.repository.OrderDetailRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 注文履歴一覧API サービス.
 */
@Service
@RequiredArgsConstructor
public class OrdersService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

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
}
