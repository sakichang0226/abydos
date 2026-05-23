package com.project.abydos.saki.api.users.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * ユーザー情報取得APIレスポンス.
 */
@Data
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserInfoResponse {

    /** ユーザーID */
    private Long userId;

    /** ユーザー名 */
    private String userName;

    /** メールアドレス */
    private String email;
}
