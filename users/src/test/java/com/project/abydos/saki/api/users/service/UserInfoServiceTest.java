package com.project.abydos.saki.api.users.service;

import com.project.abydos.saki.api.users.response.UserInfoResponse;
import com.project.abydos.saki.common.entity.UserPrincipal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class UserInfoServiceTest {

    @InjectMocks
    private UserInfoService userInfoService;

    @Test
    void UserPrincipalからユーザー情報レスポンスが生成される() {
        UserPrincipal principal = UserPrincipal.builder()
                .userId(1L)
                .userName("テストユーザー")
                .email("test@example.com")
                .authorities(List.of())
                .build();

        UserInfoResponse result = userInfoService.getUserInfo(principal);

        assertThat(result.getUserId()).isEqualTo(1L);
        assertThat(result.getUserName()).isEqualTo("テストユーザー");
        assertThat(result.getEmail()).isEqualTo("test@example.com");
    }
}
