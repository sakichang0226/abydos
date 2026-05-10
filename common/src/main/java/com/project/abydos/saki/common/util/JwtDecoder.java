package com.project.abydos.saki.common.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.project.abydos.saki.common.config.JwtProperties;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * JWTデコーダー.
 * HMAC256アルゴリズムでトークンの署名検証と有効期限チェックを行う。
 */
@Component
@RequiredArgsConstructor
public class JwtDecoder {

    private final JwtProperties properties;

    public DecodedJWT decode(@NonNull String token) {
        return JWT.require(Algorithm.HMAC256(properties.getSecret()))
                .build().verify(token);
    }
}
