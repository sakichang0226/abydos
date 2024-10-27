package com.project.abydos.saki.api.items.logic;

import com.project.abydos.saki.api.items.constant.LogMessage;
import com.project.abydos.saki.api.items.converter.ShopConverter;
import com.project.abydos.saki.api.items.response.ShopResponse;
import com.project.abydos.saki.common.constant.ErrorMessage;
import com.project.abydos.saki.common.exception.NotFoundException;
import com.project.abydos.saki.common.service.ShopService;
import com.project.schale.saki.database.product.entity.Shop;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Optional;

/**
 * 店舗情報に関するロジッククラス
 */
@Component
@RequiredArgsConstructor
public class ShopLogic {

    private final ShopService service;

    /**
     * 店舗Idに対応する店舗情報を取得し、フロントに必要な情報を返す
     * @param shopId 店舗Id
     * @return 店舗情報
     */
    public Optional<ShopResponse> findShopByShopId(@NonNull Long shopId) {

        Optional<Shop> optional = service.findOneByShopId(shopId);

        return optional.map(s -> Optional.of(ShopConverter.convert(s)))
                .orElseThrow(() -> new NotFoundException(ErrorMessage.DATA_NOT_FOUND, MessageFormat.format(LogMessage.SHOP.getMessage(), shopId)));
    }

}
