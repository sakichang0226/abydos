package com.project.abydos.saki.api.users.service;

import com.project.abydos.saki.api.users.response.UserInfoResponse;
import com.project.abydos.saki.common.entity.UserPrincipal;
import org.springframework.stereotype.Service;

/**
 * ユーザー情報取得サービス.
 * JWTのclaimsから取得済みのUserPrincipalを元にレスポンスを生成する。
 */
@Service
public class UserInfoService {

    /**
     * UserPrincipalからユーザー情報レスポンスを生成する.
     *
     * @param principal 認証済みユーザー情報
     * @return ユーザー情報レスポンス
     */
    public UserInfoResponse getUserInfo(UserPrincipal principal) {
        return new UserInfoResponse(principal.getUserId(), principal.getUserName(), principal.getEmail());
    }
}
