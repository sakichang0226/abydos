package com.project.abydos.saki.api.products.facade;

import com.project.abydos.saki.api.products.response.ProductResponse;
import com.project.abydos.saki.common.exception.DataNotFoundException;
import com.project.abydos.saki.common.service.ProductService;
import com.project.abydos.saki.dynamodb.entity.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
}
