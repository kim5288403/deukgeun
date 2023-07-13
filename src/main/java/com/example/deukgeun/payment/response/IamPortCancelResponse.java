package com.example.deukgeun.payment.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class IamPortCancelResponse {

    private Integer code;

    private String message;

    private Response response;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        private String imp_uid;
        private String channel;
        private String cancel_reason;
        private String cancel_amount;
    }
}
