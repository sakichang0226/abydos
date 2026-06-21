package com.project.abydos.saki.api.users.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    /** JWTトークン（Cookieで返却するためレスポンスボディには含めない） */
    @JsonIgnore
    private String token;

    /** ユーザー表示名 */
    private String userName;
}
