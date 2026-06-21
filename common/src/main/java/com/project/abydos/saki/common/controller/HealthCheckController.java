package com.project.abydos.saki.common.controller;

import com.project.abydos.saki.common.constant.Endpoint;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ヘルスチェック用Controller.
 * 全サービス共通で使用するため、例外的にcommonパッケージに配置しています。
 *
 * <p>注意: 基本的にControllerは各APIパッケージ(products, orders, users)に配置してください。
 */
@RestController
public class HealthCheckController {

    @GetMapping(Endpoint.HEALTH)
    public ResponseEntity<Void> healthCheck() {
        return ResponseEntity.ok().build();
    }
}
