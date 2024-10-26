package com.project.abydos.saki.api.items.converter;

import com.project.abydos.saki.api.items.response.ItemInfoResponse;
import com.project.schale.saki.database.product.entity.Item;

/**
 * エンティティクラスからフロント向けのレスポンスクラスに変換メソッドの集合
 */
public class ItemConverter {

    private ItemConverter() {}

    public static ItemInfoResponse convert(Item item) {
        return ItemInfoResponse.builder()
                    .itemId(item.getItemId())
                    .name(item.getName())
                    .shopId(item.getShopId())
                    .categoryId(item.getCategoryId())
                    .price(item.getPrice())
                    .description(item.getDescription())
                    .isStopped(item.getIsStopped())
                    .purchaseNum(item.getPurchaseNum())
                    .stock(item.getStock())
                    .build();
    }

}
