package com.project.abydos.saki.api.users.facade;

import com.project.abydos.saki.api.users.response.UserInfoResponse;
import com.project.abydos.saki.api.users.service.UserInfoService;
import com.project.abydos.saki.common.entity.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * ユーザー情報取得APIのFacade.
 * SecurityContextからUserPrincipalを取得し、Serviceへ処理を委譲する。
 */
@Component
@RequiredArgsConstructor
public class UserInfoFacade {

    private final UserInfoService userInfoService;

    /**
     * 認証済みユーザーの情報を取得する.
     *
     * @return ユーザー情報レスポンス
     */
    public UserInfoResponse getUserInfo() {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return userInfoService.getUserInfo(principal);
    }
}
