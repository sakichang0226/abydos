package com.project.abydos.saki.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * 認証済みユーザーのプリンシパル.
 * Spring SecurityのUserDetailsを実装し、SecurityContext内で認証情報を保持する。
 */
@Data
@Builder
public class UserPrincipal implements UserDetails {

    /** ユーザーID */
    private final Long userId;

    /** メールアドレス */
    private final String email;

    /** パスワード（レスポンスには含めない） */
    @JsonIgnore
    private final String password;

    /** 権限リスト */
    private final Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
