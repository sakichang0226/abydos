package com.project.abydos.saki.api.items.controller;

import com.project.abydos.saki.api.items.constant.LogMessage;
import com.project.abydos.saki.api.items.logic.CategoryLogic;
import com.project.abydos.saki.api.items.response.CategoryResponse;
import com.project.abydos.saki.common.constant.ErrorMessage;
import com.project.abydos.saki.common.exception.NotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;

import static com.project.abydos.saki.api.items.constant.EndPoint.*;

/**
 * カテゴリ情報取得APIのコントローラ層
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(API + V1)
public class CategoryController {

    private final CategoryLogic logic;

    /**
     * 指定したカテゴリIdに対応する情報と親カテゴリの情報を返却する
     * @param categoryId カテゴリId
     * @return カテゴリ情報
     */
    @GetMapping(CATEGORIES + "/{category_id}")
    public ResponseEntity<CategoryResponse> findCategoryTreeByCategoryId(@PathVariable("category_id") @Valid Long categoryId) {
        return logic.findCategoryTreeByCategoryId(categoryId).map(c -> new ResponseEntity<>(c, HttpStatus.OK))
                .orElseThrow(() ->new NotFoundException(ErrorMessage.DATA_NOT_FOUND,
                        MessageFormat.format(LogMessage.CATEGORY.getMessage(), categoryId)));
    }

}
