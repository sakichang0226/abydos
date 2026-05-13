package com.project.abydos.saki.common.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 共通エラーハンドリングで使用するログメッセージ定義.
 */
@Getter
@RequiredArgsConstructor
public enum CommonErrorLogMessage {
    /** ApiException発生時 */
    API_EXCEPTION("ApiException occurred: code={}, message={}"),
    /** バリデーションエラー発生時 */
    VALIDATION_ERROR("Validation error occurred: {}"),
    /** 存在しないエンドポイントへのアクセス時 */
    ENDPOINT_NOT_FOUND("Endpoint not found: {}"),
    /** 対象データが見つからない場合 */
    DATA_NOT_FOUND("Data not found: {}"),
    /** 想定外の例外発生時 */
    UNEXPECTED_EXCEPTION("Unexpected exception occurred: {}"),
    /** ユーザーが見つからない場合 */
    USER_NOT_FOUND("User not found: {}"),
    /** 未認証アクセス時 */
    UNAUTHORIZED_ACCESS("Unauthorized access: {}");

    private final String message;
}
