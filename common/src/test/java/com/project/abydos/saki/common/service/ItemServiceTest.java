package com.project.abydos.saki.common.service;

import com.project.abydos.saki.common.configuration.TestCommonConfiguration;
import com.project.schale.saki.database.product.entity.Item;
import com.project.schale.saki.database.product.repository.ItemRepository;
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
 * {@link com.project.abydos.saki.common.service.ItemService}に関するテストクラス
 */
@ExtendWith(SpringExtension.class)
@MybatisTest
@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ContextConfiguration(classes = {
        TestCommonConfiguration.class,
        FlywayAutoConfiguration.class
})
public class ItemServiceTest {

    private final ItemService service;

    private final ItemRepository repository;

    private final Long NOT_EXISTS_LOT = 9999L;

    /**
     * DBに対象ロットが存在するとき,エンティティクラスに詰め替えて返されるか
     */
    @Test
    public void test_DBに対象ロットが存在するとき() {
        Optional<Item> optionalItem =  service.fetchItemInfo(1L);

        assertTrue(optionalItem.isPresent());
        Item item = optionalItem.get();

        assertEquals(1, item.getItemId());
        assertEquals("Sample Item 1", item.getName());
        assertEquals(1, item.getShopId());
        assertEquals(101, item.getCategoryId());
        assertEquals(1000, item.getPrice());
        assertEquals(10, item.getPurchaseNum());
        assertEquals(50, item.getStock());
        assertFalse(item.getIsStopped());
        assertEquals("Sample description for item 1", item.getDescription());
    }

    /**
     * DBに対象ロットが存在するとき,空のOptionalが返されるか
     */
    @Test
    public void test_DBに対象ロットが存在しないとき() {
        Optional<Item> optionalItem =  service.fetchItemInfo(NOT_EXISTS_LOT);

        assertFalse(optionalItem.isPresent());
    }

}
