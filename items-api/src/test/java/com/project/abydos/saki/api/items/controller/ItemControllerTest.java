package com.project.abydos.saki.api.items.controller;


import com.project.abydos.saki.api.items.logic.ItemLogic;
import com.project.abydos.saki.api.items.response.ItemInfoResponse;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

/**
 *  {@link com.project.abydos.saki.api.items.controller.ItemController}に関するテストクラス
 */
@ExtendWith(SpringExtension.class)
public class ItemControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private ItemController controller;

    @Mock
    private ItemLogic logic;

    private final CommonExceptionHandler handler = new CommonExceptionHandler(new MockHttpServletRequest());

    private final Long ITEM_ID_RETURN_OK_RESPONSE = 1L;

    @BeforeEach
    public void setUp() {
        ItemInfoResponse response = ItemInfoResponse.builder()
                .itemId(1L)
                .name("テスト商品")
                .shopId(1L)
                .categoryId(10L)
                .price(1000L)
                .purchaseNum(0)
                .stock(100)
                .isStopped(false)
                .description("これはテスト商品")
                .imageUrl("")
                .build();

        when(logic.findItemInfo(ITEM_ID_RETURN_OK_RESPONSE)).thenReturn(Optional.of(response));

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(handler).build();
    }

    /**
     * 正常なレスポンスが返されるか
     * @throws Exception 例外
     */
    @Test
    public void test() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(API + V1 + ITEMS + "/" + 1))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * パスパラメータの型が数値出ない時、400エラーを返すか
     * @throws Exception 例外
     */
    @Test
    public void test_異常系_パスパラメータが不整合() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(API + V1 + ITEMS + "/test"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    /**
     * 存在しないパスをリクエストされたとき、404エラーを返すか
     * @throws Exception 例外
     */
    @Test
    public void test_異常系_存在しないパス() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(API + V1 + ITEMS + "/1" + "/test"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * 存在しないパスをリクエストされたとき、404エラーを返すか
     * @throws Exception 例外
     */
    @Test
    public void test_正常系_Logicクラスから空のレスポンスが返される() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(API + V1 + ITEMS + "/2"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}
