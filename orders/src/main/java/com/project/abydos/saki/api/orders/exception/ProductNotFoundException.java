package com.project.abydos.saki.api.orders.exception;

/**
 * 商品が存在しない場合の例外.
 */
public class ProductNotFoundException extends OrderException {

    public ProductNotFoundException(String detail) {
        super(OrderErrorCode.API_ORDER_ERR001, detail);
    }
}
