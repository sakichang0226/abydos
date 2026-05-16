package com.project.abydos.saki.api.orders.controller;

import com.project.abydos.saki.api.orders.facade.OrdersFacade;
import com.project.abydos.saki.api.orders.request.OrderConfirmedRequest;
import com.project.abydos.saki.api.orders.request.OrdersApiRequest;
import com.project.abydos.saki.api.orders.response.OrdersApiResponse;
import com.project.abydos.saki.common.constant.Endpoint;
import com.project.abydos.saki.common.constant.ErrorCode;
import com.project.abydos.saki.common.exception.ApiException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 注文を確定する.
     *
     * @param orderConfirmedRequest 注文確定リクエスト
     * @return 成功時は200 OK
     */
    @PostMapping(Endpoint.ORDERS)
    public ResponseEntity<Void> postOrders(@Valid @RequestBody OrderConfirmedRequest orderConfirmedRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new ApiException(ErrorCode.API_ERR001);
        }

        ordersFacade.confirmed(orderConfirmedRequest);

        return ResponseEntity.ok().build();
    }

}
