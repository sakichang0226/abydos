package com.project.abydos.saki.common.exception;

import com.project.abydos.saki.common.constant.ErrorCode;

/**
 * 対象データが見つからない場合の例外.
 */
public class DataNotFoundException extends ApiException {

    public DataNotFoundException() {
        super(ErrorCode.API_ERR004);
    }
}
