package com.project.abydos.saki.api.users.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.abydos.saki.api.users.facade.LoginFacade;
import com.project.abydos.saki.api.users.request.LoginRequest;
import com.project.abydos.saki.api.users.response.LoginResponse;
import com.project.abydos.saki.common.constant.Endpoint;
import com.project.abydos.saki.api.users.constant.UsersEndpoint;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LoginFacade loginFacade;

    @Test
    void ログイン成功時にトークンとユーザー名が返却される() throws Exception {
        LoginResponse response = new LoginResponse("test-token", "テストユーザー");
        when(loginFacade.login(any(LoginRequest.class))).thenReturn(response);

        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("password123");

        mockMvc.perform(post(Endpoint.API_PREFIX + UsersEndpoint.LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("test-token"))
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
}
