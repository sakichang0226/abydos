package com.project.abydos.saki.api.orders.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 注文確定APIのリクエストクラス
 */
@Data
public class OrderConfirmedRequest {

    /** 商品リスト */
    @Size(min = 1)
    private List<Product> products;

    /**
     * 注文商品
     */
    @Data
    public static class Product {

        /** 商品Id */
        private Long product_id;

        /** 注文数量 */
        @Min(1)
        private Long quantity;

    }

}
