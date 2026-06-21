package com.project.abydos.saki.common.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.project.abydos.saki.common.constant.SecurityConstant;
import com.project.abydos.saki.common.entity.UserPrincipal;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtToPrincipalConverterTest {

    private static final String SECRET = "test-secret";

    @Test
    void JWTからUserPrincipalが正しく変換される() {
        String token = JWT.create()
                .withSubject("1")
                .withClaim(SecurityConstant.CLAIM_EMAIL, "test@example.com")
                .sign(Algorithm.HMAC256(SECRET));

        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token);

        UserPrincipal principal = JwtToPrincipalConverter.convert(decodedJWT);

        assertThat(principal.getUserId()).isEqualTo(1L);
        assertThat(principal.getEmail()).isEqualTo("test@example.com");
        assertThat(principal.getAuthorities()).isEmpty();
    }
}
