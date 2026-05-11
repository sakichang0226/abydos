package com.project.abydos.saki.common.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * APIエラーレスポンス.
 * 異常系レスポンスの共通フォーマットとして使用する。
 */
@Data
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ErrorResponse {
    /** エラーコード */
    private String errorCode;
    /** エラーメッセージ */
    private String message;
}
