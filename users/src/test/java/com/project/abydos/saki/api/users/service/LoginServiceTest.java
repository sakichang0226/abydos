package com.project.abydos.saki.api.users.service;

import com.project.abydos.saki.api.users.request.LoginRequest;
import com.project.abydos.saki.api.users.response.LoginResponse;
import com.project.abydos.saki.common.config.JwtProperties;
import com.project.abydos.saki.common.exception.ApiException;
import com.project.abydos.saki.dynamodb.entity.User;
import com.project.abydos.saki.dynamodb.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtProperties jwtProperties;

    @InjectMocks
    private LoginService loginService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUserId(1L);
        testUser.setEmail("test@example.com");
        testUser.setPasswordHash("$2b$10$hashedpassword");
        testUser.setUserName("テストユーザー");
    }

    @Test
    void ログイン成功時にトークンとユーザー名が返却される() {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("password123");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("password123", "$2b$10$hashedpassword")).thenReturn(true);
        when(jwtProperties.getSecret()).thenReturn("test-secret-key-for-unit-test");

        LoginResponse response = loginService.login(request);

        assertThat(response.getToken()).isNotBlank();
        assertThat(response.getUser_name()).isEqualTo("テストユーザー");
    }

    @Test
    void ユーザーが存在しない場合にApiExceptionがスローされる() {
        LoginRequest request = new LoginRequest();
        request.setEmail("notfound@example.com");
        request.setPassword("password123");

        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> loginService.login(request))
                .isInstanceOf(ApiException.class);
    }

    @Test
    void パスワードが不一致の場合にApiExceptionがスローされる() {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("wrongpassword");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("wrongpassword", "$2b$10$hashedpassword")).thenReturn(false);

        assertThatThrownBy(() -> loginService.login(request))
                .isInstanceOf(ApiException.class);
    }
}
