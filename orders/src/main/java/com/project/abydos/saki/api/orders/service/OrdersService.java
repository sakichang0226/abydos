package com.project.abydos.saki.api.orders.service;

import com.project.abydos.saki.api.orders.response.OrdersApiResponse;
import com.project.abydos.saki.api.orders.constant.DeliveryStatus;
import com.project.abydos.saki.dynamodb.entity.Order;
import com.project.abydos.saki.dynamodb.entity.SubOrder;
import com.project.abydos.saki.dynamodb.repository.OrderRepository;
import com.project.abydos.saki.dynamodb.repository.PagedResult;
import com.project.abydos.saki.dynamodb.repository.SubOrderRepository;
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
    private final SubOrderRepository subOrderRepository;

    /**
     * 注文履歴一覧を取得する.
     * ordersテーブルからQueryで取得後、sub_order_idsを展開してsub_ordersテーブルからBatchGetItemで一括取得する.
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

        // sub_order_idsを展開し、BatchGetItemで一括取得
        Map<Long, Set<Long>> orderSubOrderIdsMap = orders.stream()
                .filter(o -> !CollectionUtils.isEmpty(o.getSubOrderIds()))
                .collect(Collectors.toMap(Order::getOrderId, Order::getSubOrderIds));

        Map<Long, List<SubOrder>> subOrdersByOrderId = Collections.emptyMap();
        if (!orderSubOrderIdsMap.isEmpty()) {
            List<SubOrder> subOrders = subOrderRepository.batchGetByOrderSubOrderIds(orderSubOrderIdsMap);
            subOrdersByOrderId = subOrders.stream()
                    .collect(Collectors.groupingBy(SubOrder::getOrderId));
        }

        // レスポンスマッピング
        Map<Long, List<SubOrder>> finalSubOrdersByOrderId = subOrdersByOrderId;
        List<OrdersApiResponse.OrderDetail> orderDetails = orders.stream()
                .map(order -> mapToOrderDetail(order, finalSubOrdersByOrderId.getOrDefault(order.getOrderId(), Collections.emptyList())))
                .toList();

        return OrdersApiResponse.builder()
                .lastOrderId(result.getLastEvaluatedSortKey())
                .orders(orderDetails)
                .build();
    }

    /**
     * Orderと対応するSubOrderリストから注文詳細レスポンスを生成する.
     *
     * @param order 注文エンティティ
     * @param subOrders 受注明細エンティティリスト
     * @return 注文詳細レスポンス
     */
    private OrdersApiResponse.OrderDetail mapToOrderDetail(Order order, List<SubOrder> subOrders) {
        List<OrdersApiResponse.SubOrderDetail> subOrderDetails = subOrders.stream()
                .map(this::mapToSubOrderDetail)
                .toList();

        long total = subOrders.stream()
                .mapToLong(so -> so.getPrice() * so.getOrderNum())
                .sum();

        String deliveryStatus = subOrders.isEmpty()
                ? DeliveryStatus.PROCESSING.getCode() : (
                        subOrders.stream().allMatch(so -> DeliveryStatus.DELIVERED.getCode().equals(so.getDeliveryStatus()))
                   ? DeliveryStatus.DELIVERED.getCode() : DeliveryStatus.PROCESSING.getCode());

        return OrdersApiResponse.OrderDetail.builder()
                .orderId(order.getOrderId())
                .createdAt(order.getCreatedAt())
                .total(total)
                .subOrders(subOrderDetails)
                .deliveryStatus(deliveryStatus)
                .build();
    }

    /**
     * SubOrderエンティティから受注詳細レスポンスを生成する.
     *
     * @param subOrder 受注明細エンティティ
     * @return 受注詳細レスポンス
     */
    private OrdersApiResponse.SubOrderDetail mapToSubOrderDetail(SubOrder subOrder) {
        return OrdersApiResponse.SubOrderDetail.builder()
                .subOrderId(subOrder.getSubOrderId())
                .productId(subOrder.getProductId())
                .productName(subOrder.getProductName())
                .shopId(subOrder.getShopId())
                .price(subOrder.getPrice())
                .orderNum(subOrder.getOrderNum())
                .total(subOrder.getPrice() * subOrder.getOrderNum())
                .build();
    }
}
