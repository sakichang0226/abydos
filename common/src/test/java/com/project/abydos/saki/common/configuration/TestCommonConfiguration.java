package com.project.abydos.saki.common.configuration;

import com.project.abydos.saki.common.service.CategoryService;
import com.project.schale.saki.database.product.repository.CategoryRepository;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan(basePackages = {
        "org.springframework.boot.autoconfigure.flyway",
        "com.project.abydos.saki.common",
        "com.project.schale.saki.database.product"
}, excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.project.abydos.saki.common.controller.*"))
@Profile("test")
public class TestCommonConfiguration {

    @Bean
    @DependsOn("flyway")
    public CategoryService categoryService(CategoryRepository repository) {
        return new CategoryService(repository);
    }

}
