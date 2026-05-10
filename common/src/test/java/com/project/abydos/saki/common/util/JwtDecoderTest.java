package com.project.abydos.saki.common.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.project.abydos.saki.common.config.JwtProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtDecoderTest {

    @Mock
    private JwtProperties jwtProperties;

    @InjectMocks
    private JwtDecoder jwtDecoder;

    private static final String SECRET = "test-secret";

    @BeforeEach
    void setUp() {
        when(jwtProperties.getSecret()).thenReturn(SECRET);
    }

    @Test
    void 有効なトークンが正しくデコードされる() {
        String token = JWT.create()
                .withSubject("1")
                .sign(Algorithm.HMAC256(SECRET));

        DecodedJWT decoded = jwtDecoder.decode(token);

        assertThat(decoded.getSubject()).isEqualTo("1");
    }

    @Test
    void 不正なシークレットのトークンで例外がスローされる() {
        String token = JWT.create()
                .withSubject("1")
                .sign(Algorithm.HMAC256("wrong-secret"));

        assertThatThrownBy(() -> jwtDecoder.decode(token))
                .isInstanceOf(JWTVerificationException.class);
    }
}
