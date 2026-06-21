package com.project.abydos.saki.api.orders.exception;

import com.project.abydos.saki.api.orders.constant.OrderErrorLogMessage;
import com.project.abydos.saki.common.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 注文確定API固有の例外ハンドラー.
 */
@Slf4j
@RestControllerAdvice(basePackages = "com.project.abydos.saki.api.orders")
public class OrderExceptionHandler {

    /**
     * 商品が存在しない場合の例外をハンドリングする.
     *
     * @param ex 商品未存在例外
     * @return エラーレスポンス
     */
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFoundException(ProductNotFoundException ex) {
        OrderErrorCode code = ex.getErrorCode();
        log.warn(OrderErrorLogMessage.PRODUCT_NOT_FOUND.getMessage(), ex.getDetail(), ex);
        return new ResponseEntity<>(
                new ErrorResponse(code.name(), code.getMessage()),
                code.getHttpStatus()
        );
    }

    /**
     * 商品ステータスが購入不可の場合の例外をハンドリングする.
     *
     * @param ex 商品購入不可例外
     * @return エラーレスポンス
     */
    @ExceptionHandler(ProductUnavailableException.class)
    public ResponseEntity<ErrorResponse> handleProductUnavailableException(ProductUnavailableException ex) {
        OrderErrorCode code = ex.getErrorCode();
        log.warn(OrderErrorLogMessage.PRODUCT_UNAVAILABLE.getMessage(), ex.getDetail(), ex);
        return new ResponseEntity<>(
                new ErrorResponse(code.name(), code.getMessage()),
                code.getHttpStatus()
        );
    }

    /**
     * 在庫不足の場合の例外をハンドリングする.
     *
     * @param ex 在庫不足例外
     * @return エラーレスポンス
     */
    @ExceptionHandler(OutOfStockException.class)
    public ResponseEntity<ErrorResponse> handleOutOfStockException(OutOfStockException ex) {
        OrderErrorCode code = ex.getErrorCode();
        log.warn(OrderErrorLogMessage.OUT_OF_STOCK.getMessage(), ex.getDetail(), ex);
        return new ResponseEntity<>(
                new ErrorResponse(code.name(), code.getMessage()),
                code.getHttpStatus()
        );
    }


}
