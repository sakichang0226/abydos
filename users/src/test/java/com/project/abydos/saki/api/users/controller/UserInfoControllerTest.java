package com.project.abydos.saki.api.users.controller;

import com.project.abydos.saki.api.users.constant.UsersEndpoint;
import com.project.abydos.saki.api.users.facade.UserInfoFacade;
import com.project.abydos.saki.api.users.response.UserInfoResponse;
import com.project.abydos.saki.common.constant.Endpoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
class UserInfoControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private UserInfoController userInfoController;

    @Mock
    private UserInfoFacade userInfoFacade;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userInfoController).build();
    }

    @Test
    void ユーザー情報取得成功時にユーザー情報が返却される() throws Exception {
        UserInfoResponse response = new UserInfoResponse(1L, "テストユーザー", "test@example.com");
        when(userInfoFacade.getUserInfo()).thenReturn(response);

        mockMvc.perform(get(Endpoint.API_PREFIX + UsersEndpoint.ME))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_id").value(1))
                .andExpect(jsonPath("$.user_name").value("テストユーザー"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }
}
