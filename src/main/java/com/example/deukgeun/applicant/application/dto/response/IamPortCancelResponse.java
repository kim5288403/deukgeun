package com.example.deukgeun.applicant.application.dto.response;

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
        private Integer cancel_amount;
    }
}
