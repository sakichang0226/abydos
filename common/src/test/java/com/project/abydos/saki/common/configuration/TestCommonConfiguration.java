package com.project.abydos.saki.common.configuration;

import org.springframework.context.annotation.*;

@Configuration
@ComponentScan(basePackages = {
        "com.project.abydos.saki.common",
        "com.project.schale.saki.database.product"
}, excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.project.abydos.saki.common.controller.*"))
@Profile("test")
public class TestCommonConfiguration {
}
