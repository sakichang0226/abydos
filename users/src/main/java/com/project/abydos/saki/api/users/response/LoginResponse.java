package com.project.abydos.saki.api.users.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * ログインAPIレスポンス.
 */
@Data
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LoginResponse {

    /** JWTトークン */
    private String token;

    /** ユーザー表示名 */
    private String userName;
}
