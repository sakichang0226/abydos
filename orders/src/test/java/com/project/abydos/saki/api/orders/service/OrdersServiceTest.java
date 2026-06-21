package com.project.abydos.saki.api.orders.service;

import com.project.abydos.saki.api.orders.constant.DeliveryStatus;
import com.project.abydos.saki.api.orders.exception.OutOfStockException;
import com.project.abydos.saki.api.orders.exception.ProductNotFoundException;
import com.project.abydos.saki.api.orders.exception.ProductUnavailableException;
import com.project.abydos.saki.api.orders.request.OrderConfirmedRequest;
import com.project.abydos.saki.api.orders.response.OrdersApiResponse;
import com.project.abydos.saki.dynamodb.entity.Order;
import com.project.abydos.saki.dynamodb.entity.OrderDetail;
import com.project.abydos.saki.dynamodb.entity.Product;
import com.project.abydos.saki.dynamodb.exception.StockConditionException;
import com.project.abydos.saki.dynamodb.repository.OrderRepository;
import com.project.abydos.saki.dynamodb.repository.PagedResult;
import com.project.abydos.saki.dynamodb.repository.OrderDetailRepository;
import com.project.abydos.saki.dynamodb.repository.ProductRepository;
import com.project.abydos.saki.dynamodb.repository.SequenceRepository;
import com.project.abydos.saki.dynamodb.param.OrderTransactionParam;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrdersServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderDetailRepository orderDetailRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private SequenceRepository sequenceRepository;

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
    void order_detailsがdetailIdの昇順でソートされて返却される() {
        Order order = createOrder(1L, 1001L, 1700000100000L, Set.of(1L, 2L, 3L));

        // detailIdが逆順で返却されるケース
        OrderDetail detail1 = createOrderDetail(1001L, 3L, 10001L, 1L, "商品C", 500L, 1L, "ED");
        OrderDetail detail2 = createOrderDetail(1001L, 1L, 10002L, 2L, "商品A", 1000L, 1L, "ED");
        OrderDetail detail3 = createOrderDetail(1001L, 2L, 10003L, 3L, "商品B", 2000L, 1L, "ED");

        when(orderRepository.findByUserId(1L, 10, null))
                .thenReturn(new PagedResult<>(List.of(order), null));
        when(orderDetailRepository.batchGetByOrderDetailIds(any()))
                .thenReturn(List.of(detail1, detail2, detail3));

        OrdersApiResponse response = ordersService.getOrders(1L, 10, null);

        List<OrdersApiResponse.DetailResponse> details = response.getOrders().get(0).getDetails();
        assertThat(details).hasSize(3);
        assertThat(details.get(0).getDetailId()).isEqualTo(1L);
        assertThat(details.get(1).getDetailId()).isEqualTo(2L);
        assertThat(details.get(2).getDetailId()).isEqualTo(3L);
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

    @Test
    void 注文確定_正常系() {
        Product product = createProduct(1L, "商品A", 10001L, 1000L, 10L, "O");
        when(productRepository.findByIds(List.of(1L))).thenReturn(List.of(product));
        when(sequenceRepository.getNextValue(eq("order_id"), eq(1L))).thenReturn(100L);
        when(sequenceRepository.getNextValue(eq("detail_id"), eq(1L))).thenReturn(200L);
        doNothing().when(orderRepository).saveOrder(any(OrderTransactionParam.class));

        List<OrderConfirmedRequest.Product> products = List.of(createRequestProduct(1L, 2L));

        ordersService.confirmed(1L, products);

        verify(orderRepository).saveOrder(any(OrderTransactionParam.class));
    }

    @Test
    void 注文確定_商品が存在しない場合にProductNotFoundExceptionがスローされる() {
        when(productRepository.findByIds(List.of(1L, 2L))).thenReturn(List.of(
                createProduct(1L, "商品A", 10001L, 1000L, 10L, "O")
        ));

        List<OrderConfirmedRequest.Product> products = List.of(
                createRequestProduct(1L, 1L),
                createRequestProduct(2L, 1L)
        );

        assertThatThrownBy(() -> ordersService.confirmed(1L, products))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    void 注文確定_商品ステータスが購入不可の場合にProductUnavailableExceptionがスローされる() {
        Product product = createProduct(1L, "商品A", 10001L, 1000L, 10L, "S");
        when(productRepository.findByIds(List.of(1L))).thenReturn(List.of(product));

        List<OrderConfirmedRequest.Product> products = List.of(createRequestProduct(1L, 1L));

        assertThatThrownBy(() -> ordersService.confirmed(1L, products))
                .isInstanceOf(ProductUnavailableException.class);
    }

    @Test
    void 注文確定_在庫不足の場合にOutOfStockExceptionがスローされる() {
        Product product = createProduct(1L, "商品A", 10001L, 1000L, 2L, "O");
        when(productRepository.findByIds(List.of(1L))).thenReturn(List.of(product));

        List<OrderConfirmedRequest.Product> products = List.of(createRequestProduct(1L, 5L));

        assertThatThrownBy(() -> ordersService.confirmed(1L, products))
                .isInstanceOf(OutOfStockException.class);
    }

    @Test
    void 注文確定_商品ステータスが販売終了の場合にProductUnavailableExceptionがスローされる() {
        Product product = createProduct(1L, "商品A", 10001L, 1000L, 10L, "D");
        when(productRepository.findByIds(any())).thenReturn(List.of(product));

        List<OrderConfirmedRequest.Product> products = List.of(createRequestProduct(1L, 1L));

        assertThatThrownBy(() -> ordersService.confirmed(1L, products))
                .isInstanceOf(ProductUnavailableException.class);
    }

    @Test
    void 注文確定_在庫数と注文数量が同数の場合に正常終了する() {
        Product product = createProduct(1L, "商品A", 10001L, 1000L, 5L, "O");
        when(productRepository.findByIds(any())).thenReturn(List.of(product));
        when(sequenceRepository.getNextValue(eq("order_id"), eq(1L))).thenReturn(100L);
        when(sequenceRepository.getNextValue(eq("detail_id"), eq(1L))).thenReturn(200L);
        doNothing().when(orderRepository).saveOrder(any(OrderTransactionParam.class));

        List<OrderConfirmedRequest.Product> products = List.of(createRequestProduct(1L, 5L));

        ordersService.confirmed(1L, products);

        verify(orderRepository).saveOrder(any(OrderTransactionParam.class));
    }

    @Test
    void 注文確定_同一商品が複数行ある場合に集約後の合計数量で在庫チェックされる() {
        Product product = createProduct(1L, "商品A", 10001L, 1000L, 5L, "O");
        when(productRepository.findByIds(any())).thenReturn(List.of(product));

        // 個別では在庫内（3 < 5）だが、合計すると在庫超過（3+3=6 > 5）
        List<OrderConfirmedRequest.Product> products = List.of(
                createRequestProduct(1L, 3L),
                createRequestProduct(1L, 3L)
        );

        assertThatThrownBy(() -> ordersService.confirmed(1L, products))
                .isInstanceOf(OutOfStockException.class);
    }

    @Test
    void 注文確定_同一商品が複数行あり合計数量が在庫内の場合に正常終了する() {
        Product product = createProduct(1L, "商品A", 10001L, 1000L, 10L, "O");
        when(productRepository.findByIds(any())).thenReturn(List.of(product));
        when(sequenceRepository.getNextValue(eq("order_id"), eq(1L))).thenReturn(100L);
        when(sequenceRepository.getNextValue(eq("detail_id"), eq(1L))).thenReturn(200L);
        doNothing().when(orderRepository).saveOrder(any(OrderTransactionParam.class));

        List<OrderConfirmedRequest.Product> products = List.of(
                createRequestProduct(1L, 3L),
                createRequestProduct(1L, 3L)
        );

        ordersService.confirmed(1L, products);

        verify(orderRepository).saveOrder(any(OrderTransactionParam.class));
    }

    @Test
    void 注文確定_トランザクション競合時にOutOfStockExceptionがスローされる() {
        Product product = createProduct(1L, "商品A", 10001L, 1000L, 10L, "O");
        when(productRepository.findByIds(any())).thenReturn(List.of(product));
        when(sequenceRepository.getNextValue(eq("order_id"), eq(1L))).thenReturn(100L);
        when(sequenceRepository.getNextValue(eq("detail_id"), eq(1L))).thenReturn(200L);
        doThrow(new StockConditionException("stock condition not met", new RuntimeException()))
                .when(orderRepository).saveOrder(any(OrderTransactionParam.class));

        List<OrderConfirmedRequest.Product> products = List.of(createRequestProduct(1L, 2L));

        assertThatThrownBy(() -> ordersService.confirmed(1L, products))
                .isInstanceOf(OutOfStockException.class);
    }

    private Product createProduct(Long productId, String name, Long shopId, Long price, Long stock, String status) {
        Product product = new Product();
        product.setProductId(productId);
        product.setProductName(name);
        product.setShopId(shopId);
        product.setPrice(price);
        product.setStock(stock);
        product.setStatus(status);
        return product;
    }

    private OrderConfirmedRequest.Product createRequestProduct(Long productId, Long quantity) {
        OrderConfirmedRequest.Product product = new OrderConfirmedRequest.Product();
        product.setProduct_id(productId);
        product.setQuantity(quantity);
        return product;
    }
}
