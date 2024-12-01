package com.project.abydos.saki.api.items.logic;

import com.project.abydos.saki.api.items.constant.LogMessage;
import com.project.abydos.saki.api.items.response.CategoryResponse;
import com.project.abydos.saki.common.exception.NotFoundException;
import com.project.abydos.saki.common.service.CategoryService;
import com.project.abydos.saki.common.util.JsonReader;
import com.project.schale.saki.database.product.entity.Category;
import io.github.netmikey.logunit.api.LogCapturer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

/**
 * {@link com.project.abydos.saki.api.items.logic.CategoryLogic }に関するテストクラス
 */
@ExtendWith(MockitoExtension.class)
public class CategoryLogicTest {

    @InjectMocks
    private CategoryLogic logic;

    @Mock
    private CategoryService service;

    // ログのキャプチャ設定(キャプチャ対象のクラスとログレベルを指定可能)
    @RegisterExtension
    LogCapturer logs = LogCapturer.create().captureForType(CategoryLogic.class);

    /**
     * カテゴリおよび親カテゴリの取得成功時、適切にレスポンスクラスに設定されている
     * @throws IOException JSONファイル読み込み失敗
     */
    @Test
    public void test_カテゴリおよび親カテゴリの取得成功() throws IOException {
        Category category = JsonReader.read("../resources/category.json", Category.class);
        List<Category> categories = JsonReader.readList("../resources/categories.json", Category.class);

        when(service.isEmptyCategoryTree()).thenReturn(false);
        when(service.findCategory(anyLong())).thenReturn(Optional.of(category));
        when(service.collectParentCategories(anyLong())).thenReturn(categories);

        Optional<CategoryResponse> optional = logic.findCategoryTreeByCategoryId(1L);

        assertTrue(optional.isPresent());
        CategoryResponse response = optional.get();

        assertEquals(category.getCategoryId(), response.getCategoryId());
        assertEquals(category.getName(), response.getName());

        List<CategoryResponse.ParentCategory> parentCategories = response.getParentCategories();
        assertFalse(CollectionUtils.isEmpty(parentCategories));

        assertEquals(categories.get(0).getCategoryId(), parentCategories.get(0).getCategoryId());
        assertEquals(categories.get(1).getCategoryId(), parentCategories.get(1).getCategoryId());
    }

    /**
     * カテゴリに対応する親カテゴリが存在しない場合、カテゴリの情報のみ返されるか
     * @throws IOException JSONファイル読み込み失敗
     */
    @Test
    public void test_親カテゴリが存在しない場合() throws IOException {
        Category category = JsonReader.read("../resources/root_category.json", Category.class);

        when(service.isEmptyCategoryTree()).thenReturn(false);
        when(service.findCategory(anyLong())).thenReturn(Optional.of(category));
        when(service.collectParentCategories(anyLong())).thenReturn(Collections.emptyList());

        Optional<CategoryResponse> optional = logic.findCategoryTreeByCategoryId(1L);

        assertTrue(optional.isPresent());
        CategoryResponse response = optional.get();

        assertEquals(category.getCategoryId(), response.getCategoryId());
        assertEquals(category.getName(), response.getName());

        List<CategoryResponse.ParentCategory> parentCategories = response.getParentCategories();
        assertTrue(CollectionUtils.isEmpty(parentCategories));
    }

    /**
     * カテゴリが存在しない場合、適切な例外がスローされるか
     * @throws IOException JSONファイル読み込み失敗
     */
    @Test
    public void test_カテゴリが存在しない場合() throws IOException {
        when(service.isEmptyCategoryTree()).thenReturn(false);
        when(service.findCategory(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, ()-> logic.findCategoryTreeByCategoryId(1L));
    }

    /**
     * {@link com.project.abydos.saki.common.service.CategoryService }の初期化処理が失敗、完了していない時
     * 適切に初期化処理を行っているか（初期化処理の実行ログ出力をしているか）
     * @throws IOException JSONファイル読み込み失敗
     */
    @Test
    public void test_カテゴリツリーが空の場合() throws IOException {
        Category category = JsonReader.read("../resources/root_category.json", Category.class);

        when(service.isEmptyCategoryTree()).thenReturn(true);
        when(service.findCategory(anyLong())).thenReturn(Optional.of(category));
        when(service.collectParentCategories(anyLong())).thenReturn(Collections.emptyList());


        Optional<CategoryResponse> optional = logic.findCategoryTreeByCategoryId(1L);

        assertEquals(LogMessage.INITIALIZE_CATEGORY_TREE.getMessage(), logs.getEvents().get(0).getMessage());
    }

}
