package com.project.abydos.saki.api.users.facade;

import com.project.abydos.saki.api.users.request.LoginRequest;
import com.project.abydos.saki.api.users.response.LoginResponse;
import com.project.abydos.saki.api.users.service.LoginService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginFacadeTest {

    @Mock
    private LoginService loginService;

    @InjectMocks
    private LoginFacade loginFacade;

    @Test
    void loginメソッドがServiceに処理を委譲する() {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("password123");

        LoginResponse expected = new LoginResponse("token", "テストユーザー");
        when(loginService.login(request)).thenReturn(expected);

        LoginResponse result = loginFacade.login(request);

        assertThat(result.getToken()).isEqualTo("token");
        assertThat(result.getUserName()).isEqualTo("テストユーザー");
    }
}
