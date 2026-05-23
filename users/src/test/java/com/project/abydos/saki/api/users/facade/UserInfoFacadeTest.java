package com.project.abydos.saki.api.users.facade;

import com.project.abydos.saki.api.users.response.UserInfoResponse;
import com.project.abydos.saki.api.users.service.UserInfoService;
import com.project.abydos.saki.common.entity.UserPrincipal;
import com.project.abydos.saki.common.entity.UserPrincipalAuthenticationToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserInfoFacadeTest {

    @Mock
    private UserInfoService userInfoService;

    @InjectMocks
    private UserInfoFacade userInfoFacade;

    private UserPrincipal principal;

    @BeforeEach
    void setUp() {
        principal = UserPrincipal.builder()
                .userId(1L)
                .userName("テストユーザー")
                .email("test@example.com")
                .authorities(List.of())
                .build();
        SecurityContextHolder.getContext()
                .setAuthentication(new UserPrincipalAuthenticationToken(principal));
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void SecurityContextからユーザー情報を取得しServiceに委譲する() {
        UserInfoResponse expected = new UserInfoResponse(1L, "テストユーザー", "test@example.com");
        when(userInfoService.getUserInfo(principal)).thenReturn(expected);

        UserInfoResponse result = userInfoFacade.getUserInfo();

        assertThat(result.getUserId()).isEqualTo(1L);
        assertThat(result.getUserName()).isEqualTo("テストユーザー");
        assertThat(result.getEmail()).isEqualTo("test@example.com");
    }
}
