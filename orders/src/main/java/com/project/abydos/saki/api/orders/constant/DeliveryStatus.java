package com.project.abydos.saki.api.orders.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 配送ステータス.
 * <ul>
 *   <li>PR: 処理中</li>
 *   <li>ED: 配送済み</li>
 * </ul>
 */
@Getter
@RequiredArgsConstructor
public enum DeliveryStatus {

    /** 処理中 */
    PROCESSING("PR"),
    /** 配送済み */
    DELIVERED("ED");

    private final String code;
}
