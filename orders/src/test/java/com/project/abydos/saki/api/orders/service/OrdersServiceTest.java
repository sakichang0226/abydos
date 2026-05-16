package com.project.abydos.saki.api.orders.service;

import com.project.abydos.saki.api.orders.constant.DeliveryStatus;
import com.project.abydos.saki.api.orders.response.OrdersApiResponse;
import com.project.abydos.saki.dynamodb.entity.Order;
import com.project.abydos.saki.dynamodb.entity.OrderDetail;
import com.project.abydos.saki.dynamodb.repository.OrderRepository;
import com.project.abydos.saki.dynamodb.repository.PagedResult;
import com.project.abydos.saki.dynamodb.repository.OrderDetailRepository;
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
    private OrderDetailRepository orderDetailRepository;

    @InjectMocks
    private OrdersService ordersService;

    @Test
    void 注文履歴が空の場合に空のordersが返却される() {
        when(orderRepository.findByUserId(1L, 10, null))
                .thenReturn(new PagedResult<>(Collections.emptyList(), null));

        OrdersApiResponse response = ordersService.getOrders(1L, 10, null);

        assertThat(response.getOrders()).isEmpty();
        assertThat(response.getLastOrderId()).isNull();
        verify(orderDetailRepository, never()).batchGetByOrderDetailIds(any());
    }

    @Test
    void 注文履歴が存在する場合にorder_detailsを含むレスポンスが返却される() {
        Order order = createOrder(1L, 1001L, 1700000100000L, Set.of(1L, 2L));

        OrderDetail detail1 = createOrderDetail(1001L, 1L, 10001L, 1L, "商品A", 2480L, 2L, "ED");
        OrderDetail detail2 = createOrderDetail(1001L, 2L, 10002L, 2L, "商品B", 8980L, 1L, "ED");

        when(orderRepository.findByUserId(1L, 10, null))
                .thenReturn(new PagedResult<>(List.of(order), null));
        when(orderDetailRepository.batchGetByOrderDetailIds(any()))
                .thenReturn(List.of(detail1, detail2));

        OrdersApiResponse response = ordersService.getOrders(1L, 10, null);

        assertThat(response.getOrders()).hasSize(1);
        assertThat(response.getLastOrderId()).isNull();

        OrdersApiResponse.OrderDetail orderDetail = response.getOrders().get(0);
        assertThat(orderDetail.getOrderId()).isEqualTo(1001L);
        assertThat(orderDetail.getCreatedAt()).isEqualTo(1700000100000L);
        assertThat(orderDetail.getTotal()).isEqualTo(2480L * 2 + 8980L * 1);
        assertThat(orderDetail.getDeliveryStatus()).isEqualTo(DeliveryStatus.DELIVERED.getCode());
        assertThat(orderDetail.getDetails()).hasSize(2);
    }

    @Test
    void 一部のorder_detailが未配送の場合にdelivery_statusがPRになる() {
        Order order = createOrder(1L, 1001L, 1700000100000L, Set.of(1L, 2L));

        OrderDetail detail1 = createOrderDetail(1001L, 1L, 10001L, 1L, "商品A", 2480L, 1L, "ED");
        OrderDetail detail2 = createOrderDetail(1001L, 2L, 10002L, 2L, "商品B", 8980L, 1L, "PR");

        when(orderRepository.findByUserId(1L, 10, null))
                .thenReturn(new PagedResult<>(List.of(order), null));
        when(orderDetailRepository.batchGetByOrderDetailIds(any()))
                .thenReturn(List.of(detail1, detail2));

        OrdersApiResponse response = ordersService.getOrders(1L, 10, null);

        assertThat(response.getOrders().get(0).getDeliveryStatus())
                .isEqualTo(DeliveryStatus.PROCESSING.getCode());
    }

    @Test
    void lastEvaluatedSortKeyが存在する場合にlastOrderIdが返却される() {
        Order order = createOrder(1L, 1001L, 1700000100000L, Set.of(1L));
        OrderDetail detail = createOrderDetail(1001L, 1L, 10001L, 1L, "商品A", 1000L, 1L, "ED");

        when(orderRepository.findByUserId(1L, 1, null))
                .thenReturn(new PagedResult<>(List.of(order), 1001L));
        when(orderDetailRepository.batchGetByOrderDetailIds(any()))
                .thenReturn(List.of(detail));

        OrdersApiResponse response = ordersService.getOrders(1L, 1, null);

        assertThat(response.getLastOrderId()).isEqualTo(1001L);
    }

    @Test
    void detail_idsがnullのorderはBatchGet対象外になる() {
        Order order = createOrder(1L, 1001L, 1700000100000L, null);

        when(orderRepository.findByUserId(1L, 10, null))
                .thenReturn(new PagedResult<>(List.of(order), null));

        OrdersApiResponse response = ordersService.getOrders(1L, 10, null);

        verify(orderDetailRepository, never()).batchGetByOrderDetailIds(any());
        assertThat(response.getOrders()).hasSize(1);
        assertThat(response.getOrders().get(0).getTotal()).isEqualTo(0L);
    }

    @Test
    void order_detailのtotalがprice_orderNumで計算される() {
        Order order = createOrder(1L, 1001L, 1700000100000L, Set.of(1L));
        OrderDetail detail = createOrderDetail(1001L, 1L, 10001L, 1L, "商品A", 1500L, 3L, "ED");

        when(orderRepository.findByUserId(1L, 10, null))
                .thenReturn(new PagedResult<>(List.of(order), null));
        when(orderDetailRepository.batchGetByOrderDetailIds(any()))
                .thenReturn(List.of(detail));

        OrdersApiResponse response = ordersService.getOrders(1L, 10, null);

        OrdersApiResponse.DetailResponse detailResponse = response.getOrders().get(0).getDetails().get(0);
        assertThat(detailResponse.getTotal()).isEqualTo(4500L);
        assertThat(detailResponse.getPrice()).isEqualTo(1500L);
        assertThat(detailResponse.getOrderNum()).isEqualTo(3L);
    }

    private Order createOrder(Long userId, Long orderId, Long createdAt, Set<Long> detailIds) {
        Order order = new Order();
        order.setUserId(userId);
        order.setOrderId(orderId);
        order.setCreatedAt(createdAt);
        order.setDetailIds(detailIds);
        return order;
    }

    private OrderDetail createOrderDetail(Long orderId, Long detailId, Long shopId,
                                          Long productId, String productName, Long price,
                                          Long orderNum, String deliveryStatus) {
        OrderDetail detail = new OrderDetail();
        detail.setOrderId(orderId);
        detail.setDetailId(detailId);
        detail.setShopId(shopId);
        detail.setProductId(productId);
        detail.setProductName(productName);
        detail.setPrice(price);
        detail.setOrderNum(orderNum);
        detail.setDeliveryStatus(deliveryStatus);
        return detail;
    }
}
