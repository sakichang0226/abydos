package com.project.abydos.saki.api.orders.exception;

import lombok.Getter;
import lombok.NonNull;
import org.springframework.http.HttpStatus;

/**
 * 注文確定API固有のビジネス例外.
 */
@Getter
public class OrderException extends RuntimeException {
    private final OrderErrorCode errorCode;
    private final String detail;

    /**
     * コンストラクタ.
     *
     * @param errorCode エラーコード
     * @param detail エラー詳細情報
     */
    public OrderException(@NonNull OrderErrorCode errorCode, String detail) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.detail = detail;
    }

    /**
     * HTTPステータスコードを取得する.
     *
     * @return HTTPステータス
     */
    public HttpStatus getHttpStatus() {
        return errorCode.getHttpStatus();
    }
}
