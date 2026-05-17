package com.project.abydos.saki.api.orders.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 注文確定API固有のエラーログメッセージ定義.
 */
@Getter
@RequiredArgsConstructor
public enum OrderErrorLogMessage {
    /** 商品が存在しない */
    PRODUCT_NOT_FOUND("Product not found: {}", "product_id=%s"),
    /** 商品ステータスが購入不可 */
    PRODUCT_UNAVAILABLE("Product unavailable: {}", "product_id=%s"),
    /** 在庫不足 */
    OUT_OF_STOCK("Out of stock: {}", "product_id=%s, stock=%s, quantity=%s"),
    /** トランザクション競合による在庫不足 */
    STOCK_CONDITION_CONFLICT("Transaction canceled due to condition check failure: {}", "stock condition not met");

    private final String message;
    private final String detailFormat;

    /**
     * 詳細メッセージをフォーマットする.
     *
     * @param args フォーマット引数
     * @return フォーマット済み詳細メッセージ
     */
    public String formatDetail(Object... args) {
        return String.format(detailFormat, args);
    }
}
