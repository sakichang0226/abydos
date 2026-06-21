package com.project.abydos.saki.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.abydos.saki.common.constant.CommonErrorLogMessage;
import com.project.abydos.saki.common.constant.ErrorCode;
import com.project.abydos.saki.common.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 401 Unauthorizedエラー時のカスタムエントリーポイント.
 * 未認証アクセス時にAPI_ERR003のエラーレスポンスを返却する。
 */
@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        ErrorCode code = ErrorCode.API_ERR003;
        log.warn(CommonErrorLogMessage.UNAUTHORIZED_ACCESS.getMessage(), authException.getMessage());

        response.setStatus(code.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(
                new ErrorResponse(code.name(), code.getMessage())
        ));
    }
}
