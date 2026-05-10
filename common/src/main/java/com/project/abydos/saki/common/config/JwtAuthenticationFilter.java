package com.project.abydos.saki.common.config;

import com.project.abydos.saki.common.constant.SecurityConstant;
import com.project.abydos.saki.common.entity.UserPrincipalAuthenticationToken;
import com.project.abydos.saki.common.util.JwtDecoder;
import com.project.abydos.saki.common.util.JwtToPrincipalConverter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
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
     * リクエストのAuthorizationヘッダーからBearerトークンを抽出する.
     *
     * @param request HTTPリクエスト
     * @return トークン文字列（Bearer プレフィックス除去済み）。ヘッダーが存在しない場合は空
     */
    private Optional<String> extractTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader(SecurityConstant.AUTHORIZATION_HEADER);

        if (StringUtils.hasText(token) && token.startsWith(SecurityConstant.BEARER_PREFIX)) {
            return Optional.of(token.substring(SecurityConstant.BEARER_PREFIX_LENGTH));
        }
        return Optional.empty();
    }
}
