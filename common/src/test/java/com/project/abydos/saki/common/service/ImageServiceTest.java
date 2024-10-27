package com.project.abydos.saki.common.service;

import com.project.abydos.saki.common.configuration.TestCommonConfiguration;
import com.project.schale.saki.database.product.entity.Image;
import com.project.schale.saki.database.product.repository.ImageRepository;
import io.micrometer.common.util.StringUtils;
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
 * {@link  com.project.abydos.saki.common.service.ImageService }に関するテストクラス
 */
@ExtendWith(SpringExtension.class)
@MybatisTest
@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ContextConfiguration(classes = {
        TestCommonConfiguration.class,
        FlywayAutoConfiguration.class
})
public class ImageServiceTest {

    private final ImageService imageService;

    private final ImageRepository repository;

    private final Long NOT_EXISTS_LOT = 9999L;

    /**
     * 商品に対応する画像が設定されている時,対象の商品画像が返されるか
     */
    @Test
    public void test() {
        Optional<Image> optionalImage = imageService.findImageByItemId(1L);
        assertTrue(optionalImage.isPresent());

        Image image = optionalImage.get();

        assertEquals(1, image.getImageId());
        assertEquals(1, image.getItemId());
        assertEquals("image1_item1.jpg", image.getName());
        assertTrue(StringUtils.isEmpty(image.getImageUrl()));
        assertNotNull(image.getCreateAt());
        assertNotNull(image.getUpdateAt());
    }

    /**
     * 商品に対応する画像が設定されていない時,空のOptionalが返されるか
     */
    @Test
    public void test_商品に対応する画像が設定されていない時() {
        assertFalse(imageService.findImageByItemId(NOT_EXISTS_LOT).isPresent());
    }

}
