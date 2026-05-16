package com.project.abydos.saki.api.orders.controller;

import com.project.abydos.saki.api.orders.facade.OrdersFacade;
import com.project.abydos.saki.api.orders.request.OrdersApiRequest;
import com.project.abydos.saki.api.orders.response.OrdersApiResponse;
import com.project.abydos.saki.common.constant.Endpoint;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 注文履歴一覧API コントローラー.
 */
@RestController
@RequestMapping(Endpoint.API_PREFIX)
@RequiredArgsConstructor
public class OrdersController {

    private final OrdersFacade ordersFacade;

    /**
     * 注文履歴一覧を取得する.
     *
     * @param ordersApiRequest リクエストパラメータ
     * @return 注文履歴一覧レスポンス
     */
    @GetMapping(Endpoint.ORDERS)
    public ResponseEntity<OrdersApiResponse> getOrders(@Valid OrdersApiRequest ordersApiRequest) {
        return ResponseEntity.ok(ordersFacade.getOrders(ordersApiRequest));
    }

}
