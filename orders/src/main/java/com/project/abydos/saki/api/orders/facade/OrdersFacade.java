package com.project.abydos.saki.api.orders.facade;

import com.project.abydos.saki.api.orders.request.OrderConfirmedRequest;
import com.project.abydos.saki.api.orders.request.OrdersApiRequest;
import com.project.abydos.saki.api.orders.response.OrdersApiResponse;
import com.project.abydos.saki.api.orders.service.OrdersService;
import com.project.abydos.saki.common.util.SecurityUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 注文履歴一覧API ファサード.
 */
@Component
@RequiredArgsConstructor
public class OrdersFacade {

    private final OrdersService ordersService;

    /**
     * 注文履歴一覧を取得する.
     *
     * @param ordersApiRequest リクエストパラメータ
     * @return 注文履歴一覧レスポンス
     */
    public OrdersApiResponse getOrders(@NonNull OrdersApiRequest ordersApiRequest) {

        Long seqUserId = SecurityUtils.getCurrentUserId();

        return ordersService.getOrders(seqUserId, ordersApiRequest.getLimit(), ordersApiRequest.getLastOrderId());
    }

    /**
     * 注文確定処理を実行する.
     *
     * @param orderConfirmedRequest 注文確定リクエスト
     */
    public void confirmed(@NonNull OrderConfirmedRequest orderConfirmedRequest) {
        Long seqUserId = SecurityUtils.getCurrentUserId();
        ordersService.confirmed(seqUserId, orderConfirmedRequest.getProducts());
    }

}
