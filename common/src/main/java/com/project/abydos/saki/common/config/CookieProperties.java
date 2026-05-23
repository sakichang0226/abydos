package com.project.abydos.saki.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "security.cookie")
public class CookieProperties {
    private boolean secure = true;
    private String sameSite = "Strict";
}
