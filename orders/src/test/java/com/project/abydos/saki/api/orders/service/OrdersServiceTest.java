package com.project.abydos.saki.api.orders.service;

import com.project.abydos.saki.api.orders.constant.DeliveryStatus;
import com.project.abydos.saki.api.orders.response.OrdersApiResponse;
import com.project.abydos.saki.dynamodb.entity.Order;
import com.project.abydos.saki.dynamodb.entity.SubOrder;
import com.project.abydos.saki.dynamodb.repository.OrderRepository;
import com.project.abydos.saki.dynamodb.repository.PagedResult;
import com.project.abydos.saki.dynamodb.repository.SubOrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrdersServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private SubOrderRepository subOrderRepository;

    @InjectMocks
    private OrdersService ordersService;

    @Test
    void 注文履歴が空の場合に空のordersが返却される() {
        when(orderRepository.findByUserId(1L, 10, null))
                .thenReturn(new PagedResult<>(Collections.emptyList(), null));

        OrdersApiResponse response = ordersService.getOrders(1L, 10, null);

        assertThat(response.getOrders()).isEmpty();
        assertThat(response.getLastOrderId()).isNull();
        verify(subOrderRepository, never()).batchGetByOrderSubOrderIds(any());
    }

    @Test
    void 注文履歴が存在する場合にsub_ordersを含むレスポンスが返却される() {
        Order order = createOrder(1L, 1001L, 1700000100000L, Set.of(1L, 2L));

        SubOrder subOrder1 = createSubOrder(1001L, 1L, 10001L, 1L, "商品A", 2480L, 2L, "ED");
        SubOrder subOrder2 = createSubOrder(1001L, 2L, 10002L, 2L, "商品B", 8980L, 1L, "ED");

        when(orderRepository.findByUserId(1L, 10, null))
                .thenReturn(new PagedResult<>(List.of(order), null));
        when(subOrderRepository.batchGetByOrderSubOrderIds(any()))
                .thenReturn(List.of(subOrder1, subOrder2));

        OrdersApiResponse response = ordersService.getOrders(1L, 10, null);

        assertThat(response.getOrders()).hasSize(1);
        assertThat(response.getLastOrderId()).isNull();

        OrdersApiResponse.OrderDetail detail = response.getOrders().get(0);
        assertThat(detail.getOrderId()).isEqualTo(1001L);
        assertThat(detail.getCreatedAt()).isEqualTo(1700000100000L);
        assertThat(detail.getTotal()).isEqualTo(2480L * 2 + 8980L * 1);
        assertThat(detail.getDeliveryStatus()).isEqualTo(DeliveryStatus.DELIVERED.getCode());
        assertThat(detail.getSubOrders()).hasSize(2);
    }

    @Test
    void 一部のsub_orderが未配送の場合にdelivery_statusがPRになる() {
        Order order = createOrder(1L, 1001L, 1700000100000L, Set.of(1L, 2L));

        SubOrder subOrder1 = createSubOrder(1001L, 1L, 10001L, 1L, "商品A", 2480L, 1L, "ED");
        SubOrder subOrder2 = createSubOrder(1001L, 2L, 10002L, 2L, "商品B", 8980L, 1L, "PR");

        when(orderRepository.findByUserId(1L, 10, null))
                .thenReturn(new PagedResult<>(List.of(order), null));
        when(subOrderRepository.batchGetByOrderSubOrderIds(any()))
                .thenReturn(List.of(subOrder1, subOrder2));

        OrdersApiResponse response = ordersService.getOrders(1L, 10, null);

        assertThat(response.getOrders().get(0).getDeliveryStatus())
                .isEqualTo(DeliveryStatus.PROCESSING.getCode());
    }

    @Test
    void lastEvaluatedSortKeyが存在する場合にlastOrderIdが返却される() {
        Order order = createOrder(1L, 1001L, 1700000100000L, Set.of(1L));
        SubOrder subOrder = createSubOrder(1001L, 1L, 10001L, 1L, "商品A", 1000L, 1L, "ED");

        when(orderRepository.findByUserId(1L, 1, null))
                .thenReturn(new PagedResult<>(List.of(order), 1001L));
        when(subOrderRepository.batchGetByOrderSubOrderIds(any()))
                .thenReturn(List.of(subOrder));

        OrdersApiResponse response = ordersService.getOrders(1L, 1, null);

        assertThat(response.getLastOrderId()).isEqualTo(1001L);
    }

    @Test
    void sub_order_idsがnullのorderはBatchGet対象外になる() {
        Order order = createOrder(1L, 1001L, 1700000100000L, null);

        when(orderRepository.findByUserId(1L, 10, null))
                .thenReturn(new PagedResult<>(List.of(order), null));

        OrdersApiResponse response = ordersService.getOrders(1L, 10, null);

        verify(subOrderRepository, never()).batchGetByOrderSubOrderIds(any());
        assertThat(response.getOrders()).hasSize(1);
        assertThat(response.getOrders().get(0).getTotal()).isEqualTo(0L);
    }

    @Test
    void sub_orderのtotalがprice_orderNumで計算される() {
        Order order = createOrder(1L, 1001L, 1700000100000L, Set.of(1L));
        SubOrder subOrder = createSubOrder(1001L, 1L, 10001L, 1L, "商品A", 1500L, 3L, "ED");

        when(orderRepository.findByUserId(1L, 10, null))
                .thenReturn(new PagedResult<>(List.of(order), null));
        when(subOrderRepository.batchGetByOrderSubOrderIds(any()))
                .thenReturn(List.of(subOrder));

        OrdersApiResponse response = ordersService.getOrders(1L, 10, null);

        OrdersApiResponse.SubOrderDetail subOrderDetail = response.getOrders().get(0).getSubOrders().get(0);
        assertThat(subOrderDetail.getTotal()).isEqualTo(4500L);
        assertThat(subOrderDetail.getPrice()).isEqualTo(1500L);
        assertThat(subOrderDetail.getOrderNum()).isEqualTo(3L);
    }

    private Order createOrder(Long userId, Long orderId, Long createdAt, Set<Long> subOrderIds) {
        Order order = new Order();
        order.setUserId(userId);
        order.setOrderId(orderId);
        order.setCreatedAt(createdAt);
        order.setSubOrderIds(subOrderIds);
        return order;
    }

    private SubOrder createSubOrder(Long orderId, Long subOrderId, Long shopId,
                                    Long productId, String productName, Long price,
                                    Long orderNum, String deliveryStatus) {
        SubOrder subOrder = new SubOrder();
        subOrder.setOrderId(orderId);
        subOrder.setSubOrderId(subOrderId);
        subOrder.setShopId(shopId);
        subOrder.setProductId(productId);
        subOrder.setProductName(productName);
        subOrder.setPrice(price);
        subOrder.setOrderNum(orderNum);
        subOrder.setDeliveryStatus(deliveryStatus);
        return subOrder;
    }
}
