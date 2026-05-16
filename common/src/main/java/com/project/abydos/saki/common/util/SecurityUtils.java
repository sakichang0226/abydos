package com.project.abydos.saki.common.util;

import com.project.abydos.saki.common.entity.UserPrincipal;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * SecurityContextから認証情報を取得するユーティリティ.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityUtils {

    /**
     * 認証済みユーザーのユーザーIDを取得する.
     *
     * @return ユーザーID
     */
    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        return principal.getUserId();
    }
}
