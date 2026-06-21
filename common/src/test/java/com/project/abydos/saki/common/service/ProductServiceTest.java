package com.project.abydos.saki.common.service;

import com.project.abydos.saki.dynamodb.entity.Product;
import com.project.abydos.saki.dynamodb.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void 商品IDで商品情報が取得できる() {
        Product product = new Product();
        product.setProductId(1L);
        product.setProductName("テスト商品");

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Optional<Product> result = productService.getProductById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getProductId()).isEqualTo(1L);
        assertThat(result.get().getProductName()).isEqualTo("テスト商品");
    }

    @Test
    void 存在しない商品IDの場合は空が返る() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Product> result = productService.getProductById(999L);

        assertThat(result).isEmpty();
    }

    @Test
    void 商品IDにnullを指定した場合は例外がスローされる() {
        assertThatThrownBy(() -> productService.getProductById(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void 複数商品がリクエスト順で返却される() {
        Product product1 = new Product();
        product1.setProductId(1L);
        Product product2 = new Product();
        product2.setProductId(2L);
        Product product3 = new Product();
        product3.setProductId(3L);

        // DynamoDBのBatchGetItemは順序不定のため降順で返却される想定
        List<Long> requestIds = List.of(3L, 1L, 2L);
        when(productRepository.findByIds(requestIds)).thenReturn(List.of(product1, product2, product3));

        List<Product> result = productService.getProducts(requestIds);

        assertThat(result).hasSize(3);
        assertThat(result.get(0).getProductId()).isEqualTo(3L);
        assertThat(result.get(1).getProductId()).isEqualTo(1L);
        assertThat(result.get(2).getProductId()).isEqualTo(2L);
    }

    @Test
    void 一部の商品が存在しない場合は存在する商品のみリクエスト順で返却される() {
        Product product1 = new Product();
        product1.setProductId(1L);
        Product product3 = new Product();
        product3.setProductId(3L);

        List<Long> requestIds = List.of(3L, 2L, 1L);
        when(productRepository.findByIds(requestIds)).thenReturn(List.of(product1, product3));

        List<Product> result = productService.getProducts(requestIds);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getProductId()).isEqualTo(3L);
        assertThat(result.get(1).getProductId()).isEqualTo(1L);
    }

    @Test
    void 空のリストを指定した場合は空リストが返る() {
        when(productRepository.findByIds(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<Product> result = productService.getProducts(Collections.emptyList());

        assertThat(result).isEmpty();
    }

    @Test
    void 商品IDリストにnullを指定した場合は例外がスローされる() {
        assertThatThrownBy(() -> productService.getProducts(null))
                .isInstanceOf(NullPointerException.class);
    }
}
