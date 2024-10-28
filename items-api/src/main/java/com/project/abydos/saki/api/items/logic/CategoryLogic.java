package com.project.abydos.saki.api.items.logic;

import com.project.abydos.saki.api.items.constant.LogMessage;
import com.project.abydos.saki.api.items.converter.CategoryConverter;
import com.project.abydos.saki.api.items.response.CategoryResponse;
import com.project.abydos.saki.common.constant.ErrorMessage;
import com.project.abydos.saki.common.exception.NotFoundException;
import com.project.abydos.saki.common.service.CategoryService;
import com.project.schale.saki.database.product.entity.Category;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

/**
 * カテゴリ情報をService層から取得、フロント向けに加工する
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CategoryLogic {

    private final CategoryService service;

    /**
     * 指定のカテゴリIdからカテゴリツリーを取得し、フロント向けのレスポンスクラスに加工する
     * @param categoryId カテゴリId
     * @return カテゴリ情報取得APIのレスポンスクラス
     */
    public Optional<CategoryResponse> findCategoryTreeByCategoryId(@NonNull Long categoryId) {
        /* 起動時に非同期でカテゴリツリーを作成するが、完了していない、何らかの要因で作られていない場合再度初期化　*/
        if (service.isEmptyCategoryTree()) {
            log.info(LogMessage.INITIALIZE_CATEGORY_TREE.getMessage());
            service.createCategoryTree();
        }

        Category category = service.findCategory(categoryId).orElseThrow(() ->new NotFoundException(ErrorMessage.DATA_NOT_FOUND,
                MessageFormat.format(LogMessage.CATEGORY.getMessage(), categoryId)));

        List<Category> parentCategories = service.collectParentCategories(categoryId);

        return Optional.ofNullable(CategoryConverter.convert(category, parentCategories));
    }

}
