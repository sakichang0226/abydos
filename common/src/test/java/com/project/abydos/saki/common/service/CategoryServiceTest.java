package com.project.abydos.saki.common.service;

import com.project.abydos.saki.common.configuration.TestCommonConfiguration;
import com.project.schale.saki.database.product.entity.Category;
import com.project.schale.saki.database.product.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link  com.project.abydos.saki.common.service.CategoryService }に関するテストクラス
 */
@ExtendWith(SpringExtension.class)
@MybatisTest
@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ContextConfiguration(classes = {
        TestCommonConfiguration.class
})
public class CategoryServiceTest {

    private final CategoryService service;

    private final CategoryRepository repository;

    /**
     * Spring起動時にPostConstructのメソッドが呼び出されているか
     */
    @Test
    public void test_isEmptyCategoryTree_正常系() {
        assertFalse(service.isEmptyCategoryTree());
    }

    /**
     * 指定したカテゴリIdが存在するとき、適切なカテゴリ情報が返されるか
     */
    @Test
    public void test_findCategory_正常系() {
        Optional<Category> optional = service.findCategory(10L);
        assertTrue(optional.isPresent());

        Category category = optional.get();
        assertEquals(10L, category.getCategoryId());
        assertEquals("家電", category.getName());
        assertEquals(0L, category.getParentCategoryId());
        assertEquals(1, category.getDepth());
    }

    /**
     * 指定したカテゴリIdが存在しないとき、空のOptionalが返るか
     */
    @Test
    public void test_findCategory_0件() {
        Optional<Category> optional = service.findCategory(9999L);
        assertFalse(optional.isPresent());

    }

    /**
     * 指定したカテゴリIdに親カテゴリが存在するとき、全ての親カテゴリが返されるか
     */
    @Test
    public void test_collectParentCategories_正常系() {
        List<Category> parentCategories = service.collectParentCategories(200201L);
        assertFalse(CollectionUtils.isEmpty(parentCategories));
        assertEquals(2, parentCategories.size());
        assertEquals(2002L, parentCategories.get(0).getCategoryId());
        assertEquals(parentCategories.get(1).getCategoryId(), parentCategories.get(0).getParentCategoryId());
        assertEquals(20L, parentCategories.get(1).getCategoryId());
    }

    /**
     * 指定したカテゴリIdに親カテゴリが存在しないとき、空のリストが返るか
     */
    @Test
    public void test_collectParentCategories_親カテゴリなし() {
        List<Category> parentCategories = service.collectParentCategories(10L);
        assertTrue(CollectionUtils.isEmpty(parentCategories));
    }

    /**
     * 指定したカテゴリIdが存在しないとき、空のリストが返るか
     */
    @Test
    public void test_collectParentCategories_存在しないカテゴリ() {
        List<Category> parentCategories = service.collectParentCategories(9999L);
        assertTrue(CollectionUtils.isEmpty(parentCategories));
    }

}
