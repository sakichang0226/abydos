package com.project.abydos.saki.api.items.logic;

import com.project.abydos.saki.api.items.constant.LogMessage;
import com.project.abydos.saki.api.items.converter.ItemConverter;
import com.project.abydos.saki.api.items.response.ItemInfoResponse;
import com.project.abydos.saki.common.constant.ErrorMessage;
import com.project.abydos.saki.common.exception.NotFoundException;
import com.project.abydos.saki.common.service.ImageService;
import com.project.abydos.saki.common.service.ItemService;
import com.project.schale.saki.database.product.entity.Image;
import com.project.schale.saki.database.product.entity.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Optional;

/**
 *　Controller層とService層を仲介するビジネスクラス
 */
@Component
@RequiredArgsConstructor
public class ItemLogic {

    private final ItemService itemService;

    private final ImageService imageService;

    /**
     * ロット番号に対応する商品情報を返却する
     * @param itemId ロット番号
     * @return 商品情報
     */
    public Optional<ItemInfoResponse> findItemInfo(Long itemId) {

        Item item = itemService.fetchItemInfo(itemId)
                        .orElseThrow(() -> new NotFoundException(ErrorMessage.DATA_NOT_FOUND,
                                MessageFormat.format(LogMessage.ITEM.getMessage(), itemId)));

        String imageUrl = imageService.findImageByItemId(itemId).map(Image::getImageUrl).orElse("");

        ItemInfoResponse response = ItemConverter.convert(item);
        response.setImageUrl(imageUrl);

        return Optional.of(response);
    }

}
