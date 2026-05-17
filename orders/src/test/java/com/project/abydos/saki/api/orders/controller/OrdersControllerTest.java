package com.project.abydos.saki.api.orders.controller;

import com.project.abydos.saki.api.orders.facade.OrdersFacade;
import com.project.abydos.saki.api.orders.request.OrdersApiRequest;
import com.project.abydos.saki.api.orders.response.OrdersApiResponse;
import com.project.abydos.saki.common.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.abydos.saki.api.orders.request.OrderConfirmedRequest;
import org.springframework.http.MediaType;

@ExtendWith(SpringExtension.class)
class OrdersControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OrdersFacade ordersFacade;

    @InjectMocks
    private OrdersController ordersController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(ordersController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void 注文履歴取得_正常系() throws Exception {
        OrdersApiResponse response = OrdersApiResponse.builder()
                .orders(List.of(
                        OrdersApiResponse.OrderDetail.builder()
                                .orderId(1001L)
                                .createdAt(1700000100000L)
                                .total(13940L)
                                .deliveryStatus("ED")
                                .details(List.of(
                                        OrdersApiResponse.DetailResponse.builder()
                                                .detailId(1L)
                                                .productId(1L)
                                                .productName("プレミアムコットンTシャツ")
                                                .shopId(10001L)
                                                .price(2480L)
                                                .orderNum(2L)
                                                .total(4960L)
                                                .build(),
                                        OrdersApiResponse.DetailResponse.builder()
                                                .detailId(2L)
                                                .productId(2L)
                                                .productName("ワイヤレスイヤホン Pro")
                                                .shopId(10002L)
                                                .price(8980L)
                                                .orderNum(1L)
                                                .total(8980L)
                                                .build()
                                ))
                                .build()
                ))
                .build();

        when(ordersFacade.getOrders(any(OrdersApiRequest.class))).thenReturn(response);

        mockMvc.perform(get("/api/v1/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orders.length()").value(1))
                .andExpect(jsonPath("$.orders[0].order_id").value(1001))
                .andExpect(jsonPath("$.orders[0].created_at").value(1700000100000L))
                .andExpect(jsonPath("$.orders[0].total").value(13940))
                .andExpect(jsonPath("$.orders[0].delivery_status").value("ED"))
                .andExpect(jsonPath("$.orders[0].details.length()").value(2))
                .andExpect(jsonPath("$.orders[0].details[0].detail_id").value(1))
                .andExpect(jsonPath("$.orders[0].details[0].product_name").value("プレミアムコットンTシャツ"))
                .andExpect(jsonPath("$.orders[0].details[0].price").value(2480))
                .andExpect(jsonPath("$.orders[0].details[0].order_num").value(2))
                .andExpect(jsonPath("$.orders[0].details[0].total").value(4960))
                .andExpect(jsonPath("$.orders[0].details[1].detail_id").value(2))
                .andExpect(jsonPath("$.orders[0].details[1].product_name").value("ワイヤレスイヤホン Pro"));
    }

    @Test
    void 注文履歴取得_空の場合に空配列が返却される() throws Exception {
        OrdersApiResponse response = OrdersApiResponse.builder()
                .orders(Collections.emptyList())
                .build();

        when(ordersFacade.getOrders(any(OrdersApiRequest.class))).thenReturn(response);

        mockMvc.perform(get("/api/v1/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orders").isEmpty());
    }

    @Test
    void 注文履歴取得_次ページがある場合にlastOrderIdが返却される() throws Exception {
        OrdersApiResponse response = OrdersApiResponse.builder()
                .lastOrderId(1001L)
                .orders(List.of(
                        OrdersApiResponse.OrderDetail.builder()
                                .orderId(1002L)
                                .createdAt(1700000200000L)
                                .total(2480L)
                                .deliveryStatus("PR")
                                .details(List.of(
                                        OrdersApiResponse.DetailResponse.builder()
                                                .detailId(4L)
                                                .productId(1L)
                                                .productName("プレミアムコットンTシャツ")
                                                .shopId(10001L)
                                                .price(2480L)
                                                .orderNum(1L)
                                                .total(2480L)
                                                .build()
                                ))
                                .build()
                ))
                .build();

        when(ordersFacade.getOrders(any(OrdersApiRequest.class))).thenReturn(response);

        mockMvc.perform(get("/api/v1/orders").param("limit", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.last_order_id").value(1001))
                .andExpect(jsonPath("$.orders.length()").value(1));
    }

    @Test
    void 注文履歴取得_limitが0の場合バリデーションエラー() throws Exception {
        mockMvc.perform(get("/api/v1/orders").param("limit", "0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error_code").value("API_ERR001"));
    }

    @Test
    void 注文履歴取得_limitが101の場合バリデーションエラー() throws Exception {
        mockMvc.perform(get("/api/v1/orders").param("limit", "101"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error_code").value("API_ERR001"));
    }

    @Test
    void 注文履歴取得_limitが100の場合正常() throws Exception {
        OrdersApiResponse response = OrdersApiResponse.builder()
                .orders(Collections.emptyList())
                .build();

        when(ordersFacade.getOrders(any(OrdersApiRequest.class))).thenReturn(response);

        mockMvc.perform(get("/api/v1/orders").param("limit", "100"))
                .andExpect(status().isOk());
    }

    @Test
    void 注文履歴取得_lastOrderId指定時に正常に動作する() throws Exception {
        OrdersApiResponse response = OrdersApiResponse.builder()
                .orders(Collections.emptyList())
                .build();

        when(ordersFacade.getOrders(any(OrdersApiRequest.class))).thenReturn(response);

        mockMvc.perform(get("/api/v1/orders")
                        .param("limit", "10")
                        .param("lastOrderId", "1001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orders").isEmpty());
    }

    @Test
    void 注文確定_正常系() throws Exception {
        doNothing().when(ordersFacade).confirmed(any(OrderConfirmedRequest.class));

        OrderConfirmedRequest request = new OrderConfirmedRequest();
        OrderConfirmedRequest.Product product = new OrderConfirmedRequest.Product();
        product.setProduct_id(1L);
        product.setQuantity(2L);
        request.setProducts(List.of(product));

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(ordersFacade).confirmed(any(OrderConfirmedRequest.class));
    }

    @Test
    void 注文確定_商品リストが空の場合バリデーションエラー() throws Exception {
        OrderConfirmedRequest request = new OrderConfirmedRequest();
        request.setProducts(Collections.emptyList());

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 注文確定_数量が0の場合バリデーションエラー() throws Exception {
        OrderConfirmedRequest request = new OrderConfirmedRequest();
        OrderConfirmedRequest.Product product = new OrderConfirmedRequest.Product();
        product.setProduct_id(1L);
        product.setQuantity(0L);
        request.setProducts(List.of(product));

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 注文確定_数量が負数の場合バリデーションエラー() throws Exception {
        OrderConfirmedRequest request = new OrderConfirmedRequest();
        OrderConfirmedRequest.Product product = new OrderConfirmedRequest.Product();
        product.setProduct_id(1L);
        product.setQuantity(-1L);
        request.setProducts(List.of(product));

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 注文確定_productsがnullの場合バリデーションエラー() throws Exception {
        String json = "{}";

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 注文確定_product_idがnullの場合バリデーションエラー() throws Exception {
        String json = "{\"products\":[{\"quantity\":2}]}";

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 注文確定_quantityがnullの場合バリデーションエラー() throws Exception {
        String json = "{\"products\":[{\"product_id\":1}]}";

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 注文確定_商品リストが101件の場合バリデーションエラー() throws Exception {
        List<OrderConfirmedRequest.Product> products = java.util.stream.IntStream.rangeClosed(1, 101)
                .mapToObj(i -> {
                    OrderConfirmedRequest.Product p = new OrderConfirmedRequest.Product();
                    p.setProduct_id((long) i);
                    p.setQuantity(1L);
                    return p;
                }).toList();

        OrderConfirmedRequest request = new OrderConfirmedRequest();
        request.setProducts(products);

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
