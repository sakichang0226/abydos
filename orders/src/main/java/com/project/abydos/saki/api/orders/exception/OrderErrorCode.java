package com.project.abydos.saki.api.orders.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 注文確定API固有のエラーコード定義.
 */
@Getter
@RequiredArgsConstructor
public enum OrderErrorCode {
    /** 商品が存在しない */
    API_ORDER_ERR001(HttpStatus.BAD_REQUEST, "product not found."),
    /** 商品ステータスが購入不可 */
    API_ORDER_ERR002(HttpStatus.BAD_REQUEST, "product unavailable."),
    /** 在庫不足 */
    API_ORDER_ERR003(HttpStatus.BAD_REQUEST, "out of stock.");

    private final HttpStatus httpStatus;
    private final String message;
}
