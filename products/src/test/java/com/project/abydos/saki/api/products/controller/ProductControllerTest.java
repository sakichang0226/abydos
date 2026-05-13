package com.project.abydos.saki.api.products.controller;

import com.project.abydos.saki.api.products.facade.ProductFacade;
import com.project.abydos.saki.api.products.response.ProductResponse;
import com.project.abydos.saki.api.products.response.ProductsResponse;
import com.project.abydos.saki.common.exception.DataNotFoundException;
import com.project.abydos.saki.common.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductFacade productFacade;

    @InjectMocks
    private ProductController productController;

    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        request = new MockHttpServletRequest();
    }

    @Test
    void 商品詳細取得_正常系() throws Exception {
        ProductResponse response = ProductResponse.builder()
                .productId(1L)
                .productName("テスト商品")
                .description("テスト説明")
                .imageUrl("https://example.com/image.png")
                .shopId(10L)
                .categoryId(5L)
                .price(1000L)
                .taxType("I")
                .rating(4.5)
                .reviewCount(100L)
                .stock(50L)
                .status("O")
                .createdAt(1700000000000L)
                .build();

        when(productFacade.getProduct(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.product_id").value(1))
                .andExpect(jsonPath("$.product_name").value("テスト商品"))
                .andExpect(jsonPath("$.description").value("テスト説明"))
                .andExpect(jsonPath("$.image_url").value("https://example.com/image.png"))
                .andExpect(jsonPath("$.shop_id").value(10))
                .andExpect(jsonPath("$.category_id").value(5))
                .andExpect(jsonPath("$.price").value(1000))
                .andExpect(jsonPath("$.tax_type").value("I"))
                .andExpect(jsonPath("$.rating").value(4.5))
                .andExpect(jsonPath("$.review_count").value(100))
                .andExpect(jsonPath("$.stock").value(50))
                .andExpect(jsonPath("$.status").value("O"))
                .andExpect(jsonPath("$.created_at").value(1700000000000L));
    }

    @Test
    void 商品詳細取得_存在しない商品IDの場合404() throws Exception {
        when(productFacade.getProduct(999L)).thenThrow(new DataNotFoundException());

        mockMvc.perform(get("/api/v1/products/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void 商品詳細取得_商品IDが文字列の場合バリデーションエラー() throws Exception {
        mockMvc.perform(get("/api/v1/products/abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error_code").value("API_ERR001"));
    }

    @Test
    void 商品一覧取得_正常系() throws Exception {
        ProductsResponse response = ProductsResponse.builder()
                .products(List.of(
                        ProductsResponse.Product.builder()
                                .productId(1L)
                                .productName("テスト商品1")
                                .description("テスト説明1")
                                .imageUrl("https://example.com/1.png")
                                .shopId(10L)
                                .categoryId(5L)
                                .price(1000L)
                                .taxType("I")
                                .rating(4.0)
                                .reviewCount(12L)
                                .stock(50L)
                                .status("O")
                                .createdAt(1700000000000L)
                                .build(),
                        ProductsResponse.Product.builder()
                                .productId(2L)
                                .productName("テスト商品2")
                                .description("テスト説明2")
                                .imageUrl("https://example.com/2.png")
                                .shopId(20L)
                                .categoryId(10L)
                                .price(2000L)
                                .taxType("E")
                                .rating(4.5)
                                .reviewCount(38L)
                                .stock(120L)
                                .status("O")
                                .createdAt(1700000000000L)
                                .build()
                ))
                .build();

        when(productFacade.getProducts(List.of(1L, 2L))).thenReturn(response);

        mockMvc.perform(get("/api/v1/products").param("product_ids", "1,2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products.length()").value(2))
                .andExpect(jsonPath("$.products[0].product_id").value(1))
                .andExpect(jsonPath("$.products[0].product_name").value("テスト商品1"))
                .andExpect(jsonPath("$.products[1].product_id").value(2))
                .andExpect(jsonPath("$.products[1].product_name").value("テスト商品2"));
    }

    @Test
    void 商品一覧取得_product_idsが未指定の場合バリデーションエラー() throws Exception {
        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 商品一覧取得_100件ちょうどの場合正常() throws Exception {
        String ids = LongStream.rangeClosed(1, 100)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(","));

        when(productFacade.getProducts(anyList())).thenReturn(
                ProductsResponse.builder().products(List.of()).build());

        mockMvc.perform(get("/api/v1/products").param("product_ids", ids))
                .andExpect(status().isOk());
    }

    @Test
    void 商品一覧取得_101件を超える場合バリデーションエラー() throws Exception {
        String ids = LongStream.rangeClosed(1, 101)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(","));

        mockMvc.perform(get("/api/v1/products").param("product_ids", ids))
                .andExpect(status().isBadRequest());
    }
}
