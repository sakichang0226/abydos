package com.project.abydos.saki.api.orders.exception;

/**
 * 商品ステータスが購入不可の場合の例外.
 */
public class ProductUnavailableException extends OrderException {

    public ProductUnavailableException(String detail) {
        super(OrderErrorCode.API_ORDER_ERR002, detail);
    }
}
