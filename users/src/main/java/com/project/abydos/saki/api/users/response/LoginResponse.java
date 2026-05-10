package com.project.abydos.saki.api.users.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * ログインAPIレスポンス.
 */
@Data
@AllArgsConstructor
public class LoginResponse {

    /** JWTトークン */
    private String token;

    /** ユーザー表示名 */
    private String user_name;
}
