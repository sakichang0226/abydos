package com.project.abydos.saki.api.orders.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 注文履歴一覧取得API レスポンス.
 */
@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrdersApiResponse {

    /** 最後に取得したOrderId（次ページがある場合のみ返却） */
    private Long lastOrderId;

    /** 注文一覧 */
    private List<OrderDetail> orders;

    /**
     * 注文詳細.
     */
    @Data
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class OrderDetail {

        /** 注文ID */
        private Long orderId;

        /** 注文日時（UnixTimestamp ミリ秒） */
        private Long createdAt;

        /** 合計金額 */
        private Long total;

        /** 受注一覧 */
        private List<SubOrderDetail> subOrders;

        /** 配送ステータス */
        private String deliveryStatus;
    }

    /**
     * 受注詳細.
     */
    @Data
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class SubOrderDetail {

        /** 受注ID */
        private Long subOrderId;

        /** 商品ID */
        private Long productId;

        /** 商品名 */
        private String productName;

        /** 店舗ID */
        private Long shopId;

        /** 単価 */
        private Long price;

        /** 注文数 */
        private Long orderNum;

        /** 小計（price * orderNum） */
        private Long total;
    }
}
