package com.project.abydos.saki.common.service;

import com.project.abydos.saki.dynamodb.entity.Product;
import com.project.abydos.saki.dynamodb.repository.ProductRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public List<Product> getProducts(@NonNull List<Long> productIds) {
        List<Product> products = productRepository.findByIds(productIds);

        Map<Long, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getProductId, p -> p));

        return productIds.stream()
                .map(productMap::get)
                .filter(Objects::nonNull)
                .toList();
    }

}
