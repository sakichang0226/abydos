package com.project.abydos.saki.api.items.constant;

/**
 * ログ出力周りのテンプレート<br>
 * この定数とパラメータを指定し、ログ出力するようにする
 */
public enum LogMessage {

    /** 対象商品のIdを出力する　*/
    ITEM("itemId = {0}"),
    /** 対象店舗のIdを出力する */
    SHOP("shopId = {0}"),
    /** 対象カテゴリのIdを出力する */
    CATEGORY("categoryId = {0}"),
    /** カテゴリツリーの再初期化 */
    INITIALIZE_CATEGORY_TREE("initialize Category Tree.");

    private final String message;

    private LogMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

}
