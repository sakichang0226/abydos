package com.project.abydos.saki.common.config;

import com.project.abydos.saki.common.constant.SecurityConstant;
import com.project.abydos.saki.common.entity.UserPrincipalAuthenticationToken;
import com.project.abydos.saki.common.util.JwtDecoder;
import com.project.abydos.saki.common.util.JwtToPrincipalConverter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

/**
 * JWT認証フィルター.
 * リクエストのAuthorizationヘッダーからBearerトークンを抽出し、
 * 検証・デコード後にSecurityContextへ認証情報を設定する。
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtDecoder jwtDecoder;

    /**
     * リクエストごとにJWT認証を実行する.
     * Authorizationヘッダーからトークンを抽出し、検証成功時にSecurityContextへ認証情報を設定する。
     * トークンが存在しない、または検証失敗時は認証情報を設定せずに後続フィルターへ委譲する。
     *
     * @param request  HTTPリクエスト
     * @param response HTTPレスポンス
     * @param filterChain フィルターチェーン
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        extractTokenFromRequest(request)
                .map(jwtDecoder::decode)
                .map(JwtToPrincipalConverter::convert)
                .map(UserPrincipalAuthenticationToken::new)
                .ifPresent(authentication -> SecurityContextHolder.getContext().setAuthentication(authentication));

        filterChain.doFilter(request, response);
    }

    /**
     * リクエストからトークンを抽出する.
     * Cookieの"token"を優先し、存在しない場合はAuthorizationヘッダーから取得する。
     *
     * @param request HTTPリクエスト
     * @return トークン文字列。存在しない場合は空
     */
    private Optional<String> extractTokenFromRequest(HttpServletRequest request) {
        Optional<String> cookieToken = extractTokenFromCookie(request);
        if (cookieToken.isPresent()) {
            return cookieToken;
        }

        String header = request.getHeader(SecurityConstant.AUTHORIZATION_HEADER);
        if (StringUtils.hasText(header) && header.startsWith(SecurityConstant.BEARER_PREFIX)) {
            return Optional.of(header.substring(SecurityConstant.BEARER_PREFIX_LENGTH));
        }
        return Optional.empty();
    }

    private Optional<String> extractTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }
        return Arrays.stream(cookies)
                .filter(c -> SecurityConstant.TOKEN_COOKIE_NAME.equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }
}
