package com.project.abydos.saki.api.items.controller;

import com.project.abydos.saki.api.items.logic.ItemLogic;
import com.project.abydos.saki.api.items.response.ItemInfoResponse;
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

import static com.project.abydos.saki.api.items.constant.EndPoint.*;

/**
 *　商品情報に関するコントローラー層
 */
@RestController
@RequestMapping(value = API + V1)
@RequiredArgsConstructor
public class ItemController {

    private final ItemLogic logic;

    /**
     * ロット番号に対応する商品情報を返却する
     * @param itemId ロット番号
     * @return 商品情報
     */
    @GetMapping(ITEMS + "/{item_id}")
    public ResponseEntity<ItemInfoResponse> getItemDetail(@PathVariable("item_id") @Valid Long itemId) {

        return logic.findItemInfo(itemId)
                .map(item -> new ResponseEntity<>(item, HttpStatus.OK))
                .orElseThrow(() -> new NotFoundException(ErrorMessage.DATA_NOT_FOUND, itemId));
    }

}
