package com.project.abydos.saki.api.products.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

/**
 * 商品詳細情報取得API レスポンス.
 */
@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductResponse {

    /** 商品Id */
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

    /** 税種別 */
    private String taxType;

    /** 評価スコア */
    private Double rating;

    /** レビュー件数 */
    private Long reviewCount;

    /** 在庫数 */
    private Long stock;

    /** 販売ステータス */
    private String status;

    /** 作成日 */
    private Long createdAt;
}
