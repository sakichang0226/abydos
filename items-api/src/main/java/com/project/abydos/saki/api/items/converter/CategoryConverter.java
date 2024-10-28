package com.project.abydos.saki.api.items.converter;

import com.project.abydos.saki.api.items.response.CategoryResponse;
import com.project.schale.saki.database.product.entity.Category;

import java.util.List;
import java.util.Objects;

/**
 * エンティティクラスからフロント向けのレスポンスクラスに変換メソッドの集合
 */
public class CategoryConverter {

    private CategoryConverter() {}

    /**
     * 指定のカテゴリから、カテゴリ情報取得API用のレスポンスクラスに変換する
     * @param category カテゴリツリー
     * @return 指定のカテゴリと親カテゴリ情報
     */
    public static CategoryResponse convert(Category category, List<Category> parentCategories) {
        if (Objects.isNull(category)) {
            return null;
        }

        CategoryResponse response = CategoryResponse
                .builder().categoryId(category.getCategoryId())
                .name(category.getName()).build();

        List<CategoryResponse.ParentCategory> parentsCategoriesForResponse =
                parentCategories
                        .stream()
                        .map(pc ->
                                CategoryResponse.ParentCategory.
                                        builder()
                                        .categoryId(pc.getCategoryId())
                                        .name(pc.getName())
                                        .build())
                        .toList();

        response.setParentCategories(parentsCategoriesForResponse);

        return response;
    }

}
