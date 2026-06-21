package com.project.abydos.saki.api.users.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * ログインAPIリクエスト.
 */
@Data
public class LoginRequest {

    /** メールアドレス */
    @NotBlank
    private String email;

    /** パスワード */
    @NotBlank
    private String password;
}
