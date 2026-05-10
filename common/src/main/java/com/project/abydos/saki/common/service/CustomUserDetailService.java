package com.project.abydos.saki.common.service;

import com.project.abydos.saki.common.constant.CommonErrorLogMessage;
import com.project.abydos.saki.common.entity.UserPrincipal;
import com.project.abydos.saki.dynamodb.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * UserDetailsService実装.
 * DynamoDBのusersテーブルからemailでユーザーを検索し、
 * Spring Securityの認証に必要なUserDetailsを返却する。
 */
@Component
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * メールアドレスでユーザーを検索し、認証用のUserDetailsを返却する.
     * DynamoDBのusersテーブルからemail-indexを使用して検索する。
     *
     * @param username メールアドレス
     * @return 認証用UserDetails（UserPrincipal）
     * @throws UsernameNotFoundException 指定されたメールアドレスのユーザーが存在しない場合
     */
    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        String email = username;
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format(CommonErrorLogMessage.USER_NOT_FOUND.getMessage(), email)));

        return UserPrincipal.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .authorities(List.of())
                .password(user.getPasswordHash())
                .build();
    }
}
