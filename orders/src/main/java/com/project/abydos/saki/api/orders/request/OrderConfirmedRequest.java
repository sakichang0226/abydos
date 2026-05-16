package com.project.abydos.saki.api.orders.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * 注文確定APIのリクエストクラス
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class OrderConfirmedRequest {

    /** 商品リスト */
    @Valid
    @Size(min = 1, max = 100)
    @NotEmpty
    private List<Product> products;

    /**
     * 注文商品
     */
    @Data
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class Product {

        /** 商品Id */
        @NotNull
        private Long product_id;

        /** 注文数量 */
        @Min(1)
        @NotNull
        private Long quantity;

    }

}
