package com.project.abydos.saki.api.users.controller;

import com.project.abydos.saki.api.users.constant.UsersEndpoint;
import com.project.abydos.saki.api.users.facade.UserInfoFacade;
import com.project.abydos.saki.api.users.response.UserInfoResponse;
import com.project.abydos.saki.common.constant.Endpoint;
import com.project.abydos.saki.common.entity.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ユーザー情報取得APIのController.
 * GET /api/v1/me エンドポイントを提供する。
 */
@RestController
@RequestMapping(Endpoint.API_PREFIX)
@RequiredArgsConstructor
public class UserInfoController {

    private final UserInfoFacade userInfoFacade;

    /**
     * 認証済みユーザーの情報を取得する.
     *
     * @return ユーザー情報レスポンス（userId, userName, email）
     */
    @GetMapping(UsersEndpoint.ME)
    public ResponseEntity<UserInfoResponse> getUserInfo() {
        if (!(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserPrincipal)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(userInfoFacade.getUserInfo());
    }
}
