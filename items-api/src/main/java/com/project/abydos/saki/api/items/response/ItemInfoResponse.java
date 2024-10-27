package com.project.abydos.saki.api.items.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 商品情報取得APIのレスポンスクラス
 */
@Builder
@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ItemInfoResponse {

    /** ロット番号 */
    private Long itemId;

    /** 商品名 */
    private String name;

    /** 店舗Id */
    private Long shopId;

    /** カテゴリId */
    private Long categoryId;

    /** 商品価格 */
    private Long price;

    /** 画像URL */
    private String imageUrl;

    /** 商品説明 */
    private String description;

    /** 停止フラグ */
    private Boolean isStopped;

    /** 購入数 */
    private Integer purchaseNum;

    /** 在庫 */
    private Integer stock;

}
