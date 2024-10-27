package com.project.abydos.saki.api.items.converter;

import com.project.abydos.saki.api.items.response.ShopResponse;
import com.project.schale.saki.database.product.entity.Shop;

/**
 * エンティティクラスからフロント向けのレスポンスクラスに変換メソッドの集合
 */
public class ShopConverter {

    private ShopConverter() {}

    public static ShopResponse convert(Shop shop) {
        return ShopResponse.builder().shopId(shop.getShopId()).name(shop.getName()).build();
    }

}
