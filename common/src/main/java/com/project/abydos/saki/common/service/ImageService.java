package com.project.abydos.saki.common.service;

import com.project.schale.saki.database.product.entity.Image;
import com.project.schale.saki.database.product.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 画像に関するサービス層
 */
@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository repository;

    /**
     * 商品に対応する画像情報を返却する
     * @param itemId ロット番号
     * @return 商品画像
     */
    @Transactional(readOnly = true, transactionManager = "databaseTransactionManager")
    public Optional<Image> findImageByItemId(Long itemId) {
        return repository.findOneByItemId(itemId);
    }

}
