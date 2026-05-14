package com.project.abydos.saki.api.orders.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * 注文履歴一覧取得API リクエスト.
 */
@Data
public class OrdersApiRequest {

    /** 取得件数（デフォルト10件、最大100件） */
    @Min(1)
    @Max(100)
    private Integer limit = 10;

    /** ページネーション用の最後に取得したOrderId */
    private Long lastOrderId;
}
