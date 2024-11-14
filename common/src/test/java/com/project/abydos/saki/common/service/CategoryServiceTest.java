package com.project.abydos.saki.common.service;

import com.project.abydos.saki.common.configuration.TestCommonConfiguration;
import com.project.schale.saki.database.product.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * {@link  com.project.abydos.saki.common.service.CategoryService }に関するテストクラス
 */
@ExtendWith(SpringExtension.class)
@MybatisTest
@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ContextConfiguration(classes = {
        TestCommonConfiguration.class,
        FlywayAutoConfiguration.class
})
public class CategoryServiceTest {

    private final CategoryService service;

    private final CategoryRepository repository;

    @Test
    public void test() {
    }

}
