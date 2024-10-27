package com.project.abydos.saki.api.items.controller;

import com.project.abydos.saki.api.items.constant.LogMessage;
import com.project.abydos.saki.api.items.logic.ShopLogic;
import com.project.abydos.saki.api.items.response.ShopResponse;
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
 * 店舗情報に関するコントローラー層
 */
@RestController
@RequestMapping(API + V1)
@RequiredArgsConstructor
public class ShopController {

    private final ShopLogic shopLogic;

    /**
     * 店舗Idに対応する店舗情報を取得する
     * @param shopId 店舗Id
     * @return 店舗情報のレスポンスクラス
     */
    @GetMapping(SHOP + "/{shop_id}")
    public ResponseEntity<ShopResponse> getShopInfo(@PathVariable("shop_id") @Valid Long shopId) {

        return shopLogic.findShopByShopId(shopId)
                .map(s -> new ResponseEntity<>(s, HttpStatus.OK))
                .orElseThrow(() -> new NotFoundException(ErrorMessage.DATA_NOT_FOUND,
                        MessageFormat.format(LogMessage.SHOP.getMessage(), shopId)));
    }

}
