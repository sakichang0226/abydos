package com.project.abydos.saki.common.util;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.project.abydos.saki.common.constant.SecurityConstant;
import com.project.abydos.saki.common.entity.UserPrincipal;
import lombok.NonNull;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

/**
 * JWTからUserPrincipalへの変換クラス.
 * デコード済みJWTのclaimsからユーザーID、メールアドレス、権限情報を抽出し、
 * Spring Securityの認証プリンシパルを生成する。
 */
public class JwtToPrincipalConverter {

    private JwtToPrincipalConverter() {}

    /**
     * デコード済みJWTからUserPrincipalを生成する.
     *
     * @param jwt デコード・検証済みのJWTトークン
     * @return 認証プリンシパル（userId, email, authorities を保持）
     */
    public static UserPrincipal convert(@NonNull DecodedJWT jwt) {
        return UserPrincipal.builder()
                .userId(Long.valueOf(jwt.getSubject()))
                .userName(jwt.getClaim(SecurityConstant.CLAIM_USER_NAME).asString())
                .email(jwt.getClaim(SecurityConstant.CLAIM_EMAIL).asString())
                .authorities(extractAuthorities(jwt))
                .build();
    }

    /**
     * JWTのclaimから権限リストを抽出する.
     * claim "a" が存在しない場合は空リストを返す。
     *
     * @param jwt デコード済みのJWTトークン
     * @return 権限リスト
     */
    private static List<SimpleGrantedAuthority> extractAuthorities(@NonNull DecodedJWT jwt) {
        Claim claim = jwt.getClaim(SecurityConstant.CLAIM_AUTHORITIES);
        if (claim.isNull() || claim.isMissing()) return List.of();
        return claim.asList(SimpleGrantedAuthority.class);
    }
}
