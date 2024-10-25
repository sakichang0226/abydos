package com.project.abydos.saki.common.service;

import com.project.schale.saki.database.product.entity.Item;
import com.project.schale.saki.database.product.repository.ItemRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 *　商品情報の取得に関するサービスクラス
 */
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository repository;

    /**
     * ロット番号を指定し、対象の商品を検索します
     * @param itemId ロット番号
     * @return 商品情報のOptional
     */
    @Transactional(readOnly = true, transactionManager = "databaseTransactionManager")
    public Optional<Item> fetchItemInfo(@NonNull Long itemId) {
        return repository.findByItemId(itemId);
    }

}
