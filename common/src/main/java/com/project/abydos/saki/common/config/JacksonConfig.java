package com.project.abydos.saki.common.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Jackson関連の設定のBean定義
 */
@Configuration
public class JacksonConfig {

    /**
     * Jacksonのカスタマイズ
     * 小数点が渡された際に切り捨てで整数に変換するため、バリデーションエラーになるよう設定を記載する
     *
     * @return Jackson2ObjectMapperBuilderCustomizer
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizer() {
        return builder -> builder.featuresToEnable(
                DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES
        ).featuresToDisable(
                DeserializationFeature.ACCEPT_FLOAT_AS_INT
        );
    }
}
