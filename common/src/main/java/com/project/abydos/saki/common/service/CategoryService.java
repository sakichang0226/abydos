package com.project.abydos.saki.common.service;

import com.project.schale.saki.database.product.entity.Category;
import com.project.schale.saki.database.product.repository.CategoryRepository;
import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 *
 */
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    Map<Long, Category> categoryTrees = new HashMap<>();

    /**
     *
     */
    @PostConstruct
    @Async
    public void createCategoryTree() {
        List<Category> categories = categoryRepository.findAll();
        List<Category> sortedCategories = categories.stream().sorted(Comparator.comparing(Category::getDepth)).toList();

        for (Category category : sortedCategories) {
            if (!categoryTrees.containsKey(category.getCategoryId())) {
                categoryTrees.put(category.getCategoryId(), category);
            }
        }
    }

    /**
     *
     * @param categoryId
     * @return
     */
    public Optional<Category> findCategory(@NonNull Long categoryId) {
        return categoryTrees.containsKey(categoryId) ? Optional.of(categoryTrees.get(categoryId)) : Optional.empty();
    }


    /**
     * 指定のカテゴリの親カテゴリを集計し、リスト形式で返却する
     * @param categoryId 対象のカテゴリ
     * @return 親カテゴリ情報のリスト
     */
    public List<Category> collectParentCategories(@NonNull Long categoryId) {
        List<Category> parentCategories = new ArrayList<>();

        if (!categoryTrees.containsKey(categoryId)) {
            return Collections.emptyList();
        }

        Category category = categoryTrees.get(categoryId);

        while (categoryTrees.containsKey(category.getParentCategoryId())) {
            category = categoryTrees.get(category.getParentCategoryId());

            parentCategories.add(category);
        }

        return parentCategories;
    }


    public Map<Long, Category> getCategoryTrees() {

        if (isEmptyCategoryTree()) {
            createCategoryTree();
        }

        return this.categoryTrees;
    }

    /**
     * カテゴリツリーが初期化済みでないか判定する
     * @return 初期化済みでない場合,True
     */
    public boolean isEmptyCategoryTree() {
        return CollectionUtils.isEmpty(categoryTrees);
    }

}
