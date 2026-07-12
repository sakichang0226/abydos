package com.project.abydos.saki.common.config;

import ch.qos.logback.access.tomcat.LogbackValve;
import org.springframework.boot.web.embedded.tomcat.ConfigurableTomcatWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * tomcatのアクセスログをカスタムフォーマットで出すためのBean定義
 */
@Configuration
public class LogbackAccessConfig {

    @Bean
    public WebServerFactoryCustomizer<ConfigurableTomcatWebServerFactory> webServerFactoryCustomizer() {
        return (factory) -> {
            LogbackValve v = new LogbackValve();
            factory.addEngineValves(v);
        };
    }
}
