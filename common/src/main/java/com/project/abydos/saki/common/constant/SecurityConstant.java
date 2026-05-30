package com.project.abydos.saki.common.constant;

/**
 * Spring Security・JWT関連の定数定義.
 */
public class SecurityConstant {

    /** Authorizationヘッダー名 */
    public static final String AUTHORIZATION_HEADER = "Authorization";

    /** Bearerトークンプレフィックス */
    public static final String BEARER_PREFIX = "Bearer ";

    /** Bearerプレフィックスの文字数 */
    public static final int BEARER_PREFIX_LENGTH = BEARER_PREFIX.length();

    /** JWT claim: メールアドレス */
    public static final String CLAIM_EMAIL = "email";

    /** JWT claim: 権限 */
    public static final String CLAIM_AUTHORITIES = "a";

    /** JWT claim: ユーザー名 */
    public static final String CLAIM_USER_NAME = "name";

    /** JWTの有効期限（時間） */
    public static final long TOKEN_EXPIRATION_HOURS = 1;

    /** Cookie名: トークン */
    public static final String TOKEN_COOKIE_NAME = "token";

    private SecurityConstant() {}
}
