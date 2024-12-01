package com.project.abydos.saki.api.items.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * カテゴリ情報取得APIのレスポンスクラス
 */
@Builder
@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CategoryResponse {

    /** カテゴリId */
    private Long categoryId;

    /** 対象カテゴリ名 */
    private String name;

    /** 親カテゴリ */
    private List<ParentCategory> parentCategories;

    /**
     * カテゴリIdに対応する親カテゴリの情報をまとめたレスポンスクラス
     */
    @Builder
    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ParentCategory {

        /** カテゴリId */
        private Long categoryId;

        /** 対象カテゴリ名 */
        private String name;
    }

}
