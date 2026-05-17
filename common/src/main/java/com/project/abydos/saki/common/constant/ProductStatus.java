package com.project.abydos.saki.common.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 商品販売ステータス定義.
 */
@Getter
@RequiredArgsConstructor
public enum ProductStatus {
    /** 販売中 */
    ON_SALE("O"),
    /** 売切 */
    SOLD_OUT("S"),
    /** 販売終了 */
    DISCONTINUED("D");

    private final String code;

    /**
     * 購入可能なステータスかどうかを判定する.
     *
     * @return 販売中の場合true
     */
    public boolean isAvailable() {
        return this == ON_SALE;
    }
}
