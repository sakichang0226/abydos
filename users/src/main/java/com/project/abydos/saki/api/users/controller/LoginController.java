package com.project.abydos.saki.api.users.controller;

import com.project.abydos.saki.api.users.request.LoginRequest;
import com.project.abydos.saki.api.users.response.LoginResponse;
import com.project.abydos.saki.api.users.facade.LoginFacade;
import com.project.abydos.saki.api.users.constant.UsersEndpoint;
import com.project.abydos.saki.common.constant.Endpoint;
import com.project.abydos.saki.common.constant.SecurityConstant;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ログインAPIのController.
 * POST /api/v1/login エンドポイントを提供する。
 */
@RestController
@RequestMapping(Endpoint.API_PREFIX)
@RequiredArgsConstructor
public class LoginController {

    private final LoginFacade loginFacade;

    /**
     * ログイン処理を実行する.
     *
     * @param request メールアドレスとパスワードを含むリクエスト
     * @return ユーザー名を含むレスポンス（トークンはSet-Cookieヘッダーで返却）
     */
    @PostMapping(UsersEndpoint.LOGIN)
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = loginFacade.login(request);

        ResponseCookie cookie = ResponseCookie.from(SecurityConstant.TOKEN_COOKIE_NAME, response.getToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(SecurityConstant.TOKEN_EXPIRATION_HOURS * 3600)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(response);
    }
}
