package com.project.abydos.saki.api.items.logic;

import com.project.abydos.saki.api.items.response.ItemInfoResponse;
import com.project.abydos.saki.common.exception.NotFoundException;
import com.project.abydos.saki.common.service.ImageService;
import com.project.abydos.saki.common.service.ItemService;
import com.project.schale.saki.database.product.entity.Image;
import com.project.schale.saki.database.product.entity.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * {@link com.project.abydos.saki.api.items.logic.ItemLogic }に関するテストクラス
 */
@ExtendWith(MockitoExtension.class)
public class ItemLogicTest {

    @InjectMocks
    private ItemLogic logic;

    @Mock
    private ItemService itemService;

    @Mock
    private ImageService imageService;

    private final Long EXISTS_ITEM_ID = 1L;

    private final Long NOT_FOUND_ITEM_ID = 9999L;

    private Item item;

    private Image image;

    @BeforeEach
    public void setUp() {
        item = new Item();

        item.setItemId(1L);
        item.setName("テスト商品");
        item.setShopId(1L);
        item.setCategoryId(10L);
        item.setPrice(1000L);
        item.setPurchaseNum(0);
        item.setStock(100);
        item.setIsStopped(false);
        item.setDescription("これはテスト商品");

        image = new Image();

        image.setItemId(item.getItemId());
        image.setName("テスト商品");
        image.setImageUrl("http://localhost:8080/test_img.jpg");
        image.setImageId(1L);
        image.setUpdateAt(LocalDateTime.now());
        image.setCreateAt(LocalDateTime.now());
    }

    /**
     *　商品と対象の商品画像が設定されているとき、レスポンス項目に適切な値が設定されているか
     */
    @Test
    public void test_商品と対応する商品画像が存在するとき() {
        when(itemService.fetchItemInfo(EXISTS_ITEM_ID)).thenReturn(Optional.of(item));
        when(imageService.findImageByItemId(EXISTS_ITEM_ID)).thenReturn(Optional.of(image));

        Optional<ItemInfoResponse> optional = logic.findItemInfo(EXISTS_ITEM_ID);

        assertTrue(optional.isPresent());
        ItemInfoResponse response = optional.get();

        assertEquals(item.getItemId(), response.getItemId());
        assertEquals(item.getName(), response.getName());
        assertEquals(item.getItemId(), response.getItemId());
        assertEquals(item.getCategoryId(), response.getCategoryId());
        assertEquals(item.getPrice(), response.getPrice());
        assertEquals(image.getImageUrl(), response.getImageUrl());
        assertEquals(item.getDescription(), response.getDescription());
        assertEquals(item.getPurchaseNum(), response.getPurchaseNum());
        assertEquals(item.getStock(), response.getStock());
        assertEquals(item.getIsStopped(), response.getIsStopped());
    }

    /**
     *　対象の商品が存在しないとき、適切な例外がスローされるか
     */
    @Test
    public void test_存在しない商品を指定した時() {
        when(itemService.fetchItemInfo(NOT_FOUND_ITEM_ID)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class,() -> logic.findItemInfo(NOT_FOUND_ITEM_ID));
    }

    /**
     * 対象の商品は存在するが、商品画像を設定していない時、商品情報の画像URLはデフォルト値か
     */
    @Test
    public void test_商品に対応する画像が存在しない時() {
        when(itemService.fetchItemInfo(EXISTS_ITEM_ID)).thenReturn(Optional.of(item));
        when(imageService.findImageByItemId(EXISTS_ITEM_ID)).thenReturn(Optional.empty());

        Optional<ItemInfoResponse> optional = logic.findItemInfo(EXISTS_ITEM_ID);

        assertTrue(optional.isPresent());
        ItemInfoResponse response = optional.get();

        assertEquals("", response.getImageUrl());
    }

}
