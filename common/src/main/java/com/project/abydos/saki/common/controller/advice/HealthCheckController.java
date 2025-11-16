package com.project.abydos.saki.common.controller.advice;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ロードバランサーやデプロイ先のヘルスチェック用のエンドポイントを管理するController<br>
 * ここには、エンドポイントは設けないこと
 */
@RestController
@RequiredArgsConstructor
public class HealthCheckController {

    /**
     * ロードバランサ―、コンテナのヘルスチェック用のAPI
     * @return ステータスOK
     */
    @GetMapping("/healthCheck")
    public ResponseEntity<String> healthCheck() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
