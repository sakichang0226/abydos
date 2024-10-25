package com.project.abydos.saki.common.constant;

/**
 * エラーレスポンスに関するエラーコードおよびメッセージを定義する定数クラス
 */
public enum ErrorCodes {

    INTERNAL_SERVER_ERROR("API_ERR000", "[{0}] internal Server Error."),
    VALIDATION_ERROR("API_ERR001", "[{0}] validation Check Error."),
    DATA_NOT_FOUND("API_ERR002", "[{0}] data not found.");

    private final String code;

    private final String message;

    private ErrorCodes(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getErrorCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

}
