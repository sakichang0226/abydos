package com.project.abydos.saki.api.users.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.project.abydos.saki.api.users.request.LoginRequest;
import com.project.abydos.saki.api.users.response.LoginResponse;
import com.project.abydos.saki.common.config.JwtProperties;
import com.project.abydos.saki.common.constant.ErrorCode;
import com.project.abydos.saki.common.constant.SecurityConstant;
import com.project.abydos.saki.common.exception.ApiException;
import com.project.abydos.saki.dynamodb.entity.User;
import com.project.abydos.saki.dynamodb.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * ログインサービス.
 * メールアドレスでのユーザー検索、BCryptパスワード検証、JWTトークン生成を行う。
 */
@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProperties jwtProperties;

    /**
     * ログイン処理を実行する.
     * <ol>
     *   <li>emailでusersテーブルからユーザーを検索</li>
     *   <li>BCryptPasswordEncoderでパスワードを検証</li>
     *   <li>JWTトークンを生成（claims: user_id, email、有効期限: 1時間）</li>
     * </ol>
     *
     * @param request ログインリクエスト（email, password）
     * @return JWTトークンとユーザー名を含むレスポンス
     * @throws ApiException ユーザーが存在しない、またはパスワードが不正な場合（API_LOGIN_ERR001）
     */
    public LoginResponse login(@NonNull LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ApiException(ErrorCode.API_LOGIN_ERR001));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new ApiException(ErrorCode.API_LOGIN_ERR001);
        }

        String token = JWT.create()
                .withSubject(user.getUserId().toString())
                .withClaim(SecurityConstant.CLAIM_EMAIL, user.getEmail())
                .withClaim(SecurityConstant.CLAIM_USER_NAME, user.getUserName())
                .withExpiresAt(Instant.now().plus(SecurityConstant.TOKEN_EXPIRATION_HOURS, ChronoUnit.HOURS))
                .sign(Algorithm.HMAC256(jwtProperties.getSecret()));

        return new LoginResponse(token, user.getUserName());
    }
}
