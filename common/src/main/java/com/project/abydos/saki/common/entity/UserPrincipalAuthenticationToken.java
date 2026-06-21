package com.project.abydos.saki.common.entity;

import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * JWT認証済みユーザーの認証トークン.
 * JwtAuthenticationFilterで検証成功後にSecurityContextへ設定される。
 */
public class UserPrincipalAuthenticationToken extends AbstractAuthenticationToken {
    private final UserPrincipal userPrincipal;

    public UserPrincipalAuthenticationToken(UserPrincipal userPrincipal) {
        super(userPrincipal.getAuthorities());
        this.userPrincipal = userPrincipal;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public UserPrincipal getPrincipal() {
        return userPrincipal;
    }
}
