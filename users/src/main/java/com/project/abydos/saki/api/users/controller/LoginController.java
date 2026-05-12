package com.project.abydos.saki.api.users.controller;

import com.project.abydos.saki.api.users.request.LoginRequest;
import com.project.abydos.saki.api.users.response.LoginResponse;
import com.project.abydos.saki.api.users.facade.LoginFacade;
import com.project.abydos.saki.api.users.constant.UsersEndpoint;
import com.project.abydos.saki.common.constant.Endpoint;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
     * @return JWTトークンとユーザー名を含むレスポンス
     */
    @PostMapping(UsersEndpoint.LOGIN)
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(loginFacade.login(request));
    }
}
