package com.project.abydos.saki.api.products.controller;

import com.project.abydos.saki.api.products.constant.ProductEndPoint;
import com.project.abydos.saki.api.products.facade.ProductFacade;
import com.project.abydos.saki.api.products.response.ProductResponse;
import com.project.abydos.saki.api.products.response.ProductsResponse;
import com.project.abydos.saki.common.constant.Endpoint;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品APIコントローラー.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(Endpoint.API_PREFIX)
public class ProductController {

    private final ProductFacade productFacade;

    /**
     * 商品詳細情報を取得する.
     *
     * @param productId 商品ID
     * @return 商品詳細レスポンス
     */
    @GetMapping(ProductEndPoint.PRODUCTS + "/{product_id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable("product_id") Long productId) {
        return ResponseEntity.ok(productFacade.getProduct(productId));
    }


    /**
     * 複数の商品情報を取得する.
     *
     * @param productIds 商品IDリスト
     * @return 商品一覧レスポンス
     */
    @GetMapping(ProductEndPoint.PRODUCTS)
    public ResponseEntity<ProductsResponse> getProducts(
            @RequestParam("product_ids")
            @Size(min = 1, max = 100) List<Long> productIds) {
        return ResponseEntity.ok(productFacade.getProducts(productIds));
    }

}
