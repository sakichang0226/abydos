package com.project.abydos.saki.common.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * APIエラーコード定義.
 * HttpStatusとエラーメッセージを一元管理する。
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    /** バリデーションエラー */
    API_ERR001(HttpStatus.BAD_REQUEST, "validation check error."),
    /** 対象のエンドポイントが存在しない */
    API_ERR002(HttpStatus.NOT_FOUND, "not found."),
    /** 未ログイン時のアクセス */
    API_ERR003(HttpStatus.UNAUTHORIZED, "unauthorized."),
    /** メールアドレスまたはパスワードが不正 */
    API_LOGIN_ERR001(HttpStatus.UNAUTHORIZED, "invalid password or email."),
    /** 対象データが存在しない */
    API_ERR004(HttpStatus.NOT_FOUND, "data not found."),
    /** サーバー内部エラー */
    API_ERR999(HttpStatus.INTERNAL_SERVER_ERROR, "server error.");

    private final HttpStatus httpStatus;
    private final String message;
}
