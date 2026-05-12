package com.project.abydos.saki.api.users.facade;

import com.project.abydos.saki.api.users.request.LoginRequest;
import com.project.abydos.saki.api.users.response.LoginResponse;
import com.project.abydos.saki.api.users.service.LoginService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * ログインAPIのFacade.
 * Controllerからのリクエストを受け、適切なServiceへ処理を委譲する。
 */
@Component
@RequiredArgsConstructor
public class LoginFacade {

    private final LoginService loginService;

    /**
     * ログイン処理を実行する.
     *
     * @param request ログインリクエスト（email, password）
     * @return JWTトークンとユーザー名を含むレスポンス
     */
    public LoginResponse login(@NonNull LoginRequest request) {
        return loginService.login(request);
    }
}
