package com.project.abydos.saki.api.orders.facade;

import com.project.abydos.saki.api.orders.request.OrderConfirmedRequest;
import com.project.abydos.saki.api.orders.request.OrdersApiRequest;
import com.project.abydos.saki.api.orders.response.OrdersApiResponse;
import com.project.abydos.saki.api.orders.service.OrdersService;
import com.project.abydos.saki.common.util.SecurityUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrdersFacadeTest {

    @Mock
    private OrdersService ordersService;

    @InjectMocks
    private OrdersFacade ordersFacade;

    @Test
    void JWTから取得したuserIdでOrdersServiceが呼び出される() {
        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getCurrentUserId).thenReturn(1L);

            OrdersApiRequest request = new OrdersApiRequest();
            request.setLimit(10);
            request.setLastOrderId(null);

            OrdersApiResponse expected = OrdersApiResponse.builder()
                    .orders(Collections.emptyList())
                    .build();
            when(ordersService.getOrders(1L, 10, null)).thenReturn(expected);

            OrdersApiResponse response = ordersFacade.getOrders(request);

            assertThat(response.getOrders()).isEmpty();
        }
    }

    @Test
    void lastOrderIdが指定された場合にServiceに渡される() {
        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getCurrentUserId).thenReturn(2L);

            OrdersApiRequest request = new OrdersApiRequest();
            request.setLimit(5);
            request.setLastOrderId(100L);

            OrdersApiResponse expected = OrdersApiResponse.builder()
                    .orders(Collections.emptyList())
                    .lastOrderId(100L)
                    .build();
            when(ordersService.getOrders(2L, 5, 100L)).thenReturn(expected);

            OrdersApiResponse response = ordersFacade.getOrders(request);

            assertThat(response.getLastOrderId()).isEqualTo(100L);
        }
    }

    @Test
    void 注文確定_JWTから取得したuserIdでServiceのconfirmedが呼び出される() {
        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getCurrentUserId).thenReturn(1L);

            OrderConfirmedRequest request = new OrderConfirmedRequest();
            OrderConfirmedRequest.Product product = new OrderConfirmedRequest.Product();
            product.setProduct_id(10L);
            product.setQuantity(2L);
            request.setProducts(List.of(product));

            doNothing().when(ordersService).confirmed(1L, request.getProducts());

            ordersFacade.confirmed(request);

            verify(ordersService).confirmed(1L, request.getProducts());
        }
    }
}
