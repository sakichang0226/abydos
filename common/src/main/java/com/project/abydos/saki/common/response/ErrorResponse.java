package com.project.abydos.saki.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * APIエラーレスポンス.
 * 異常系レスポンスの共通フォーマットとして使用する。
 */
@Data
@AllArgsConstructor
public class ErrorResponse {
    /** エラーコード */
    private String error_code;
    /** エラーメッセージ */
    private String message;
}
