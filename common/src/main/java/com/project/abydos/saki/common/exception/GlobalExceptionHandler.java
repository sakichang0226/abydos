package com.project.abydos.saki.common.exception;

import com.project.abydos.saki.common.constant.CommonErrorLogMessage;
import com.project.abydos.saki.common.constant.ErrorCode;
import com.project.abydos.saki.common.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * 全API共通の例外ハンドラー.
 * 各種例外をキャッチし、統一されたエラーレスポンス形式で返却する。
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 対象データが見つからない場合の例外ハンドラー.
     */
    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDataNotFoundException(DataNotFoundException ex) {
        ErrorCode code = ex.getErrorCode();
        log.warn(CommonErrorLogMessage.DATA_NOT_FOUND.getMessage(), ex.getMessage(), ex);
        return new ResponseEntity<>(
                new ErrorResponse(code.name(), code.getMessage()),
                code.getHttpStatus()
        );
    }

    /**
     * APIビジネス例外ハンドラー.
     */
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException ex) {
        ErrorCode code = ex.getErrorCode();
        log.error(CommonErrorLogMessage.API_EXCEPTION.getMessage(), code.name(), code.getMessage(), ex);
        return new ResponseEntity<>(
                new ErrorResponse(code.name(), code.getMessage()),
                code.getHttpStatus()
        );
    }

    /**
     * バリデーションエラーハンドラー.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        ErrorCode code = ErrorCode.API_ERR001;
        log.warn(CommonErrorLogMessage.VALIDATION_ERROR.getMessage(), ex.getMessage(), ex);
        return new ResponseEntity<>(
                new ErrorResponse(code.name(), code.getMessage()),
                code.getHttpStatus()
        );
    }

    /**
     * 存在しないエンドポイントへのアクセス時のハンドラー.
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFound(NoResourceFoundException ex) {
        ErrorCode code = ErrorCode.API_ERR002;
        log.warn(CommonErrorLogMessage.ENDPOINT_NOT_FOUND.getMessage(), ex.getMessage(), ex);
        return new ResponseEntity<>(
                new ErrorResponse(code.name(), code.getMessage()),
                code.getHttpStatus()
        );
    }

    /**
     * 想定外の例外ハンドラー.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        ErrorCode code = ErrorCode.API_ERR999;
        log.error(CommonErrorLogMessage.UNEXPECTED_EXCEPTION.getMessage(), ex.getMessage(), ex);
        return new ResponseEntity<>(
                new ErrorResponse(code.name(), code.getMessage()),
                code.getHttpStatus()
        );
    }
}
