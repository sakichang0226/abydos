package com.project.abydos.saki.api.products.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 複数商品取得レスポンス.
 */
@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductsResponse {

    /** 商品リスト */
    @Builder.Default
    private List<Product> products = new ArrayList<>();

    /**
     * 商品情報.
     */
    @Data
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Product {

        /** ロット番号 */
        private Long productId;

        /** 商品名 */
        private String productName;

        /** 商品説明 */
        private String description;

        /** 商品画像URL */
        private String imageUrl;

        /** 店舗Id */
        private Long shopId;

        /** カテゴリId */
        private Long categoryId;

        /** 価格 */
        private Long price;

        /** 税種別 (I:内税 / E:外税 / N:税無し) */
        private String taxType;

        /** 評価スコア */
        private Double rating;

        /** レビュー件数 */
        private Long reviewCount;

        /** 在庫数 */
        private Long stock;

        /** 販売ステータス (O:販売中 / S:売切 / D:販売終了) */
        private String status;

        /** 作成日 (UnixTimestampミリ秒) */
        private Long createdAt;
    }
}
