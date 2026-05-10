package com.project.abydos.saki.common.exception;

import com.project.abydos.saki.common.constant.ErrorCode;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.http.HttpStatus;

/**
 * APIビジネス例外.
 * {@link ErrorCode}を指定してスローすることで、
 * GlobalExceptionHandlerが適切なHTTPステータスとエラーレスポンスを返却する。
 */
@Getter
public class ApiException extends RuntimeException {
    private final ErrorCode errorCode;

    public ApiException(@NonNull ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public HttpStatus getHttpStatus() {
        return errorCode.getHttpStatus();
    }
}
