package com.project.abydos.saki.api.products.facade;

import com.project.abydos.saki.api.products.response.ProductResponse;
import com.project.abydos.saki.api.products.response.ProductsResponse;
import com.project.abydos.saki.common.exception.DataNotFoundException;
import com.project.abydos.saki.common.service.ProductService;
import com.project.abydos.saki.dynamodb.entity.Product;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 商品APIファサード.
 */
@Component
@RequiredArgsConstructor
public class ProductFacade {

    private final ProductService productService;

    /**
     * 商品詳細情報を取得し、レスポンスを生成する.
     *
     * @param productId 商品ID
     * @return 商品詳細レスポンス
     * @throws DataNotFoundException 対象商品が存在しない場合
     */
    public ProductResponse getProduct(@NonNull Long productId) {
        Product p = productService.getProductById(productId)
                .orElseThrow(DataNotFoundException::new);

        return ProductResponse.builder()
                .productId(p.getProductId())
                .productName(p.getProductName())
                .description(p.getDescription())
                .imageUrl(p.getImageUrl())
                .shopId(p.getShopId())
                .categoryId(p.getCategoryId())
                .price(p.getPrice())
                .taxType(p.getTaxType())
                .rating(p.getRating())
                .reviewCount(p.getReviewCount())
                .stock(p.getStock())
                .status(p.getStatus())
                .createdAt(p.getCreatedAt())
                .build();
    }

    /**
     * 複数の商品情報を取得し、レスポンスを生成する.
     *
     * @param productIds 商品IDリスト
     * @return 商品一覧レスポンス
     */
    public ProductsResponse getProducts(List<Long> productIds) {
        List<Product> products = productService.getProducts(productIds);

        return ProductsResponse.builder()
            .products(products.stream().map(p -> ProductsResponse.Product.builder()
                    .productId(p.getProductId())
                    .productName(p.getProductName())
                    .description(p.getDescription())
                    .imageUrl(p.getImageUrl())
                    .shopId(p.getShopId())
                    .categoryId(p.getCategoryId())
                    .price(p.getPrice())
                    .taxType(p.getTaxType())
                    .rating(p.getRating())
                    .reviewCount(p.getReviewCount())
                    .stock(p.getStock())
                    .status(p.getStatus())
                    .createdAt(p.getCreatedAt())
                    .build()).toList())
            .build();
    }
}
