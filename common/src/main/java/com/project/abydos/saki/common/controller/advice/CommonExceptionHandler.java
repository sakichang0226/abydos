package com.project.abydos.saki.common.controller.advice;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import com.project.abydos.saki.common.exception.NotFoundException;
import com.project.abydos.saki.common.exception.RequestInvalidException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import jakarta.validation.ConstraintViolationException;

import java.text.MessageFormat;

import static com.project.abydos.saki.common.constant.ErrorCodes.*;

/**
 *　
 */
@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class CommonExceptionHandler {

    private final HttpServletRequest request;

    /**
     * リクエスト内容に問題があるときにハンドリングする共通例外
     *
     * @param exception 例外クラス
     * @return API_ERR001のエラーレスポンス
     */
    @ExceptionHandler(RequestInvalidException.class)
    public ResponseEntity<ErrorInfo> handleException(RequestInvalidException exception){
        log.warn(exception.getMessage());

        return new ResponseEntity<>(
                createErrorResponse(VALIDATION_ERROR.getErrorCode(), VALIDATION_ERROR.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    /**
     * リクエスト内容に問題があるときにハンドリングする共通例外
     *
     * @param exception 例外クラス
     * @return API_ERR001のエラーレスポンス
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorInfo> handleException(ConstraintViolationException exception){
        log.warn(exception.getMessage());

        return new ResponseEntity<>(
                createErrorResponse(VALIDATION_ERROR.getErrorCode(), VALIDATION_ERROR.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    /**
     * 対象のデータがなかった時にハンドリングする共通例外
     *
     * @param exception 例外クラス
     * @return API_ERR002のエラーレスポンス
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorInfo> handleException(NotFoundException exception){
        log.warn(exception.getMessage());

        return new ResponseEntity<>(
                createErrorResponse(DATA_NOT_FOUND.getErrorCode(), DATA_NOT_FOUND.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    /**
     * 対象のデータがなかった時にハンドリングする共通例外
     *
     * @param exception 例外クラス
     * @return API_ERR002のエラーレスポンス
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorInfo> handleException(NoResourceFoundException exception){
        log.warn(exception.getMessage());

        return new ResponseEntity<>(
                createErrorResponse(DATA_NOT_FOUND.getErrorCode(), DATA_NOT_FOUND.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    /**
     * 個別でハンドリングしていない例外クラスは内部エラーで返すようにハンドリングする
     *
     * @param exception 例外クラス
     * @return API_ERR000のエラーレスポンス
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorInfo> handleException(RuntimeException exception){
        log.error(exception.getMessage());

        return new ResponseEntity<>(
                createErrorResponse(INTERNAL_SERVER_ERROR.getErrorCode(), INTERNAL_SERVER_ERROR.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    /**
     * 例外ハンドリングで返す共通のエラーレスポンスを作成する
     * @param errorCode エラーコード
     * @param message　エラーメッセージ
     * @return エラーレスポンス
     */
    private ErrorInfo createErrorResponse(String errorCode, String message){
        return ErrorInfo.builder()
                .errorCode(errorCode)
                .message(MessageFormat.format(message, request.getRequestURI())).build();
    }

    @Builder
    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    private static class ErrorInfo {

        /** エラーコード */
        private String errorCode;

        /** エラーメッセージ */
        private String message;

    }

}
