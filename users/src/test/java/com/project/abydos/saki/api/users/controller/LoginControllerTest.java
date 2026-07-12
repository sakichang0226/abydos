package com.project.abydos.saki.api.users.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.abydos.saki.api.users.constant.UsersEndpoint;
import com.project.abydos.saki.api.users.facade.LoginFacade;
import com.project.abydos.saki.api.users.request.LoginRequest;
import com.project.abydos.saki.api.users.response.LoginResponse;
import com.project.abydos.saki.common.config.CookieProperties;
import com.project.abydos.saki.common.constant.Endpoint;
import com.project.abydos.saki.common.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * {@link LoginController}に関するテストクラス
 */
@ExtendWith(SpringExtension.class)
class LoginControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private LoginController loginController;

    @Mock
    private LoginFacade loginFacade;

    @Mock
    private CookieProperties cookieProperties;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        when(cookieProperties.isSecure()).thenReturn(true);
        when(cookieProperties.getSameSite()).thenReturn("Strict");
        mockMvc = MockMvcBuilders.standaloneSetup(loginController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void ログイン成功時にCookieにトークンが設定されユーザー名が返却される() throws Exception {
        LoginResponse response = new LoginResponse("test-token", "テストユーザー");
        when(loginFacade.login(any(LoginRequest.class))).thenReturn(response);

        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("password123");

        mockMvc.perform(post(Endpoint.API_PREFIX + UsersEndpoint.LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(cookie().value("token", "test-token"))
                .andExpect(cookie().httpOnly("token", true))
                .andExpect(cookie().secure("token", true))
                .andExpect(cookie().path("token", "/"))
                .andExpect(cookie().maxAge("token", 3600))
                .andExpect(jsonPath("$.token").doesNotExist())
                .andExpect(jsonPath("$.user_name").value("テストユーザー"));
    }

    @Test
    void メールアドレスが空の場合にバリデーションエラーが返却される() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("");
        request.setPassword("password123");

        mockMvc.perform(post(Endpoint.API_PREFIX + UsersEndpoint.LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error_code").value("API_ERR001"));
    }

    @Test
    void パスワードが空の場合にバリデーションエラーが返却される() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("");

        mockMvc.perform(post(Endpoint.API_PREFIX + UsersEndpoint.LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error_code").value("API_ERR001"));
    }

    @Test
    void ログアウト成功時にCookieが無効化され204が返却される() throws Exception {
        mockMvc.perform(post(Endpoint.API_PREFIX + UsersEndpoint.LOGOUT))
                .andExpect(status().isNoContent())
                .andExpect(cookie().value("token", ""))
                .andExpect(cookie().httpOnly("token", true))
                .andExpect(cookie().secure("token", true))
                .andExpect(cookie().path("token", "/"))
                .andExpect(cookie().maxAge("token", 0));
    }

    @Test
    void リクエストにCookieが設定されていない場合でも204が返却される() throws Exception {
        mockMvc.perform(post(Endpoint.API_PREFIX + UsersEndpoint.LOGOUT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(cookie().value("token", ""))
                .andExpect(cookie().maxAge("token", 0));
    }
}
