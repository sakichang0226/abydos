package com.project.abydos.saki.api.items.controller;

import com.project.abydos.saki.api.items.logic.ShopLogic;
import com.project.abydos.saki.api.items.response.ShopResponse;
import com.project.abydos.saki.common.controller.advice.CommonExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static com.project.abydos.saki.api.items.constant.EndPoint.*;
import static org.mockito.Mockito.when;

/**
 *  {@link com.project.abydos.saki.api.items.controller.ShopController}に関するテストクラス
 */
@ExtendWith(SpringExtension.class)
public class ShopControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private ShopController controller;

    @Mock
    private ShopLogic logic;

    private final CommonExceptionHandler handler = new CommonExceptionHandler(new MockHttpServletRequest());

    private final Long SHOP_ID_RETURN_OK_RESPONSE = 1L;

    private final Long NOT_EXISTS_SHOP_ID = 9999L;

    @BeforeEach
    public void setUp() {
        ShopResponse response = ShopResponse.builder().shopId(1L).name("テスト店舗").build();

        when(logic.findShopByShopId(SHOP_ID_RETURN_OK_RESPONSE)).thenReturn(Optional.of(response));
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(handler).build();
    }

    /**
     * 正常なレスポンスが返されるか
     * @throws Exception 例外
     */
    @Test
    public void test_正常系() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(API + V1 + SHOP + "/" + SHOP_ID_RETURN_OK_RESPONSE))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * パスパラメータの型が不正なリクエストをされたとき、400エラーを返すか
     * @throws Exception 例外
     */
    @Test
    public void test_異常系_パスパラメータの型が不正なリクエストをされたとき() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(API + V1 + SHOP + "/test"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    /**
     * 存在しないパスをリクエストされたとき、404エラーを返すか
     * @throws Exception 例外
     */
    @Test
    public void test_異常系_存在しないパスをリクエストされたとき() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(API + V1 + SHOP + "/" + NOT_EXISTS_SHOP_ID + "/test"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * 存在しない店舗をリクエストされたとき、404エラーを返すか
     * @throws Exception 例外
     */
    @Test
    public void test_異常系_存在しない店舗をリクエストされたとき() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(API + V1 + SHOP + "/" + NOT_EXISTS_SHOP_ID))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}
