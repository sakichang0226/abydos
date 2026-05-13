package com.project.abydos.saki.common.service;

import com.project.abydos.saki.dynamodb.entity.Product;
import com.project.abydos.saki.dynamodb.repository.ProductRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 商品サービス.
 */
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * 商品IDから商品情報を取得する.
     *
     * @param productId 商品ID
     * @return 商品情報
     */
    public Optional<Product> getProductById(@NonNull Long productId) {
        return productRepository.findById(productId);
    }

    /**
     * 指定した商品IDに紐づく商品情報を取得する.
     *
     * @param productIds 複数の商品Id
     * @return 商品情報
     */
    public List<Product> getProducts(List<Long> productIds) {
        return productRepository.findByIds(productIds);
    }

}
