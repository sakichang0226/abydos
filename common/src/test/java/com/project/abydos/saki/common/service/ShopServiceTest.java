package com.project.abydos.saki.common.service;

import com.project.abydos.saki.common.configuration.TestCommonConfiguration;
import com.project.schale.saki.database.product.entity.Shop;
import com.project.schale.saki.database.product.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link com.project.abydos.saki.common.service.ShopService}に関するテストクラス
 */
@ExtendWith(SpringExtension.class)
@MybatisTest
@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ContextConfiguration(classes = {
        TestCommonConfiguration.class,
        FlywayAutoConfiguration.class
})
public class ShopServiceTest {

    private final ShopService service;

    private final ShopRepository shopRepository;

    private final Long NOT_EXISTS_SHOP = 9999L;

    /**
     * 対象店舗が存在する時,エンティティクラスにV2_insert.sqlで定義したレコードが設定されているか
     */
    @Test
    public void test_対象店舗が存在する時() {
        Optional<Shop> optional = service.findOneByShopId(1L);

        assertTrue(optional.isPresent());
        Shop shop = optional.get();

        assertEquals(1, shop.getShopId());
        assertEquals("Shop Alpha", shop.getName());
    }

    /**
     * 対象店舗が存在しない時,空のOptionalが返るか
     */
    @Test
    public void test_対象店舗が存在しない時() {
        Optional<Shop> optional = service.findOneByShopId(NOT_EXISTS_SHOP);

        assertFalse(optional.isPresent());
    }

}
