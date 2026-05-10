package com.project.abydos.saki.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * JWT関連の設定プロパティ.
 * application.yamlの {@code security.jwt} 配下の値をバインドする。
 */
@Data
@Configuration
@ConfigurationProperties("security.jwt")
public class JwtProperties {

    /** JWTの署名・検証に使用するHMAC256シークレットキー */
    private String secret;
}
