package com.project.abydos.saki.api.products.facade;

import com.project.abydos.saki.api.products.response.ProductResponse;
import com.project.abydos.saki.api.products.response.ProductsResponse;
import com.project.abydos.saki.common.exception.DataNotFoundException;
import com.project.abydos.saki.common.service.ProductService;
import com.project.abydos.saki.dynamodb.entity.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductFacadeTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductFacade productFacade;

    @Test
    void 商品が存在する場合レスポンスが返却される() {
        Product product = new Product();
        product.setProductId(1L);
        product.setProductName("テスト商品");
        product.setDescription("テスト説明");
        product.setImageUrl("https://example.com/image.png");
        product.setShopId(10L);
        product.setCategoryId(5L);
        product.setPrice(1000L);
        product.setTaxType("I");
        product.setRating(4.5);
        product.setReviewCount(100L);
        product.setStock(50L);
        product.setStatus("O");
        product.setCreatedAt(1700000000000L);

        when(productService.getProductById(1L)).thenReturn(Optional.of(product));

        ProductResponse response = productFacade.getProduct(1L);

        assertThat(response.getProductId()).isEqualTo(1L);
        assertThat(response.getProductName()).isEqualTo("テスト商品");
        assertThat(response.getDescription()).isEqualTo("テスト説明");
        assertThat(response.getImageUrl()).isEqualTo("https://example.com/image.png");
        assertThat(response.getShopId()).isEqualTo(10L);
        assertThat(response.getCategoryId()).isEqualTo(5L);
        assertThat(response.getPrice()).isEqualTo(1000L);
        assertThat(response.getTaxType()).isEqualTo("I");
        assertThat(response.getRating()).isEqualTo(4.5);
        assertThat(response.getReviewCount()).isEqualTo(100L);
        assertThat(response.getStock()).isEqualTo(50L);
        assertThat(response.getStatus()).isEqualTo("O");
        assertThat(response.getCreatedAt()).isEqualTo(1700000000000L);
    }

    @Test
    void 商品が存在しない場合DataNotFoundExceptionがスローされる() {
        when(productService.getProductById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productFacade.getProduct(999L))
                .isInstanceOf(DataNotFoundException.class);
    }

    @Test
    void 複数商品が存在する場合一覧レスポンスが返却される() {
        Product product1 = new Product();
        product1.setProductId(1L);
        product1.setProductName("テスト商品1");
        product1.setDescription("テスト説明1");
        product1.setImageUrl("https://example.com/1.png");
        product1.setShopId(10L);
        product1.setCategoryId(5L);
        product1.setPrice(1000L);
        product1.setTaxType("I");
        product1.setRating(4.0);
        product1.setReviewCount(12L);
        product1.setStock(50L);
        product1.setStatus("O");
        product1.setCreatedAt(1700000000000L);

        Product product2 = new Product();
        product2.setProductId(2L);
        product2.setProductName("テスト商品2");
        product2.setDescription("テスト説明2");
        product2.setImageUrl("https://example.com/2.png");
        product2.setShopId(20L);
        product2.setCategoryId(10L);
        product2.setPrice(2000L);
        product2.setTaxType("E");
        product2.setRating(4.5);
        product2.setReviewCount(38L);
        product2.setStock(120L);
        product2.setStatus("O");
        product2.setCreatedAt(1700000000000L);

        when(productService.getProducts(List.of(1L, 2L))).thenReturn(List.of(product1, product2));

        ProductsResponse response = productFacade.getProducts(List.of(1L, 2L));

        assertThat(response.getProducts()).hasSize(2);
        assertThat(response.getProducts().get(0).getProductId()).isEqualTo(1L);
        assertThat(response.getProducts().get(0).getProductName()).isEqualTo("テスト商品1");
        assertThat(response.getProducts().get(1).getProductId()).isEqualTo(2L);
        assertThat(response.getProducts().get(1).getProductName()).isEqualTo("テスト商品2");
    }

    @Test
    void 商品が0件の場合空リストが返却される() {
        when(productService.getProducts(List.of(999L))).thenReturn(List.of());

        ProductsResponse response = productFacade.getProducts(List.of(999L));

        assertThat(response.getProducts()).isEmpty();
    }
}
