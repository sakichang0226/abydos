package com.project.abydos.saki.api.items.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 店舗情報取得APIのレスポンスクラス
 */
@Getter
@Setter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ShopResponse {

    /** 店舗Id　*/
    private Long shopId;

    /**　店舗名　*/
    private String name;
}
