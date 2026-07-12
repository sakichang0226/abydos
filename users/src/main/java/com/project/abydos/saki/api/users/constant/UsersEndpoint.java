package com.project.abydos.saki.api.users.constant;

/**
 * usersパッケージのエンドポイントパス定義.
 */
public class UsersEndpoint {
    /** ログイン */
    public static final String LOGIN = "/login";

    /**
     * ログアウトAPI
     */
    public static final String LOGOUT = "/logout";

    /** ユーザー情報取得 */
    public static final String ME = "/me";

    private UsersEndpoint() {}
}
