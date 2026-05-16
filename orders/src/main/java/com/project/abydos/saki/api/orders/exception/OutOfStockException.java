package com.project.abydos.saki.api.orders.exception;

/**
 * 在庫不足の場合の例外.
 */
public class OutOfStockException extends OrderException {

    public OutOfStockException(String detail) {
        super(OrderErrorCode.API_ORDER_ERR003, detail);
    }
}
