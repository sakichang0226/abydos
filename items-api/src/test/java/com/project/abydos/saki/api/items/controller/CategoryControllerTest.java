package com.project.abydos.saki.api.items.controller;

import com.project.abydos.saki.api.items.logic.CategoryLogic;
import com.project.abydos.saki.api.items.response.CategoryResponse;
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
 *  {@link com.project.abydos.saki.api.items.controller.CategoryController}に関するテストクラス
 */
@ExtendWith(SpringExtension.class)
public class CategoryControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private CategoryController controller;

    @Mock
    private CategoryLogic logic;

    private final CommonExceptionHandler handler = new CommonExceptionHandler(new MockHttpServletRequest());

    private final Long NOT_EXISTS_CATEGORY_ID = 9999L;

    @BeforeEach
    public void setUp() {
        CategoryResponse response = CategoryResponse.builder().build();
        when(logic.findCategoryTreeByCategoryId(anyLong())).thenReturn(Optional.of(response));

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(handler).build();
    }

    /**
     * 存在するカテゴリIdを指定した時、200系が返るか
     * @throws Exception 例外
     */
    @Test
    public void test_正常系() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(API + V1 + CATEGORIES + "/30"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * パスパラメータの型が数値出ない時、400エラーを返すか
     * @throws Exception 例外
     */
    @Test
    public void test_異常系_パスパラメータが不整合() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(API + V1 + CATEGORIES + "/test"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    /**
     * 存在しないカテゴリIdを指定した時、404エラーを返すか
     * @throws Exception 例外
     */
    @Test
    public void test_異常系_存在しないカテゴリIdを指定した時() throws Exception {
        when(logic.findCategoryTreeByCategoryId(NOT_EXISTS_CATEGORY_ID)).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.get(API + V1 + CATEGORIES + "/" + NOT_EXISTS_CATEGORY_ID))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}
