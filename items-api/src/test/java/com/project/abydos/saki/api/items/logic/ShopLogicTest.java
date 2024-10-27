package com.project.abydos.saki.api.items.logic;

import com.project.abydos.saki.api.items.response.ShopResponse;
import com.project.abydos.saki.common.exception.NotFoundException;
import com.project.abydos.saki.common.service.ShopService;
import com.project.schale.saki.database.product.entity.Shop;
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
 * {@link com.project.abydos.saki.api.items.logic.ShopLogic }に関するテストクラス
 */
@ExtendWith(MockitoExtension.class)
public class ShopLogicTest {

    @InjectMocks
    private ShopLogic shopLogic;

    @Mock
    private ShopService shopService;

    private Shop shop;

    private final Long EXISTS_SHOP_ID = 1L;

    private final Long NOT_EXISTS_SHOP_ID = 9999L;

    @BeforeEach
    public void setUp() {
        shop = new Shop();

        shop.setShopId(1L);
        shop.setName("テスト店舗");
        shop.setCreateAt(LocalDateTime.now());
        shop.setUpdateAt(LocalDateTime.now());
    }

    /**
     * 対象の店舗が存在する時,フロント向けに必要な情報が設定されているか
     */
    @Test
    public void test_対象の店舗が存在する時() {
        when(shopService.findOneByShopId(EXISTS_SHOP_ID)).thenReturn(Optional.of(shop));

        Optional<ShopResponse> optional = shopLogic.findShopByShopId(EXISTS_SHOP_ID);
        assertTrue(optional.isPresent());
        ShopResponse response = optional.get();

        assertEquals(shop.getShopId(), response.getShopId());
        assertEquals(shop.getName(), response.getName());
    }

    /**
     * 対象の店舗が存在しない時,エラーレスポンスに対応する例外がスローされるか
     */
    @Test
    public void test_対象の店舗が存在しない時() {
        assertThrows(NotFoundException.class, ()-> shopLogic.findShopByShopId(NOT_EXISTS_SHOP_ID));
    }
}
