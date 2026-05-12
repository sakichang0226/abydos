package com.project.abydos.saki.api.products.controller;

import com.project.abydos.saki.api.products.constant.ProductEndPoint;
import com.project.abydos.saki.api.products.facade.ProductFacade;
import com.project.abydos.saki.api.products.response.ProductResponse;
import com.project.abydos.saki.common.constant.Endpoint;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<ProductResponse> getProduct(@Valid @PathVariable("product_id") Long productId) {
        return ResponseEntity.ok(productFacade.getProduct(productId));
    }

}
