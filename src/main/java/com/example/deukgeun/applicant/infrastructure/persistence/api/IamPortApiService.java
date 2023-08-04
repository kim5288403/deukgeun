package com.example.deukgeun.applicant.infrastructure.persistence.api;

import com.example.deukgeun.applicant.application.dto.request.CancelRequest;
import com.example.deukgeun.applicant.application.dto.response.IamPortCancelResponse;
import com.example.deukgeun.applicant.application.dto.response.IamPortResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

@Service
public class IamPortApiService {
    public String getIamPortAuthToken(String iamPortApiKey, String iamPortApiSecret) {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://api.iamport.kr/users/getToken")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build()
                .mutate()
                .build();

        IamPortResponse response = webClient
                .post()
                .uri(UriBuilder::build)
                .body(BodyInserters.fromValue("{ \"imp_key\": \"" + iamPortApiKey + "\", \"imp_secret\": \"" + iamPortApiSecret + "\" }"))
                .retrieve()
                .bodyToMono(IamPortResponse.class)
                .block();

        assert response != null;
        return response.getResponse().getAccess_token();
    }

    public IamPortCancelResponse cancelIamPort(CancelRequest request) throws Exception {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://api.iamport.kr/payments/cancel")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, request.getAccessToken())
                .build()
                .mutate()
                .build();

        IamPortCancelResponse response = webClient
                .post()
                .uri(UriBuilder::build)
                .body(BodyInserters.fromValue("{ \"imp_uid\": \"" + request.getImpUid() + "\", \"amount\": \"" + request.getAmount() + "\" }"))
                .retrieve()
                .bodyToMono(IamPortCancelResponse.class)
                .block();

        if (response.getCode() == 1) {
            throw new Exception(response.getMessage());
        }

        return response;
    }
}
