package com.example.deukgeun.payment.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IamPortResponse {

    private AccessTokenResponse response;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AccessTokenResponse {
        private String access_token;
    }

}
