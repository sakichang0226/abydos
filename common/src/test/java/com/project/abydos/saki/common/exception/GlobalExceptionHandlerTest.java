package com.project.abydos.saki.common.exception;

import com.project.abydos.saki.common.constant.ErrorCode;
import com.project.abydos.saki.common.response.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void ApiException発生時に対応するエラーコードが返却される() {
        ApiException ex = new ApiException(ErrorCode.API_ERR002);

        ResponseEntity<ErrorResponse> response = handler.handleApiException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getErrorCode()).isEqualTo("API_ERR002");
        assertThat(response.getBody().getMessage()).isEqualTo("data not found.");
    }

    @Test
    void 想定外の例外発生時にAPI_ERR999が返却される() {
        Exception ex = new RuntimeException("unexpected error");

        ResponseEntity<ErrorResponse> response = handler.handleException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody().getErrorCode()).isEqualTo("API_ERR999");
        assertThat(response.getBody().getMessage()).isEqualTo("server error.");
    }
}
