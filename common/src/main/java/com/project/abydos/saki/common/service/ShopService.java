package com.project.abydos.saki.common.service;

import com.project.schale.saki.database.product.entity.Shop;
import com.project.schale.saki.database.product.repository.ShopRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 店舗情報に関するサービス層
 */
@Service
@RequiredArgsConstructor
public class ShopService {

    private final ShopRepository repository;

    /**
     * 店舗Idから対象の店舗情報を取得する
     * @param shopId 店舗Id
     * @return 店舗情報（Optionalのため、0件の場合、空を返却します。）
     */
    public Optional<Shop> findOneByShopId(@NonNull Long shopId) {
        return repository.findOne(shopId);
    }

}
