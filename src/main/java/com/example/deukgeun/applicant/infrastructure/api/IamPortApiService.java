package com.example.deukgeun.applicant.infrastructure.api;

import com.example.deukgeun.applicant.application.dto.request.CancelRequest;
import com.example.deukgeun.applicant.application.dto.response.IamPortCancelResponse;
import com.example.deukgeun.applicant.application.dto.response.IamPortResponse;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import java.io.IOException;

@Service
public class IamPortApiService {
    private final IamportClient iamportClient;
    private final String API_KEY;
    private final String API_SECRET;
    private final String GET_TOKEN_URL;
    private final String CANCEL_URL;



    public IamPortApiService(@Value("${iamPort.api.key}") String apiKey,
                             @Value("${iamPort.api.secret}") String secretKey,
                             @Value("${iamPort.url.getToken}") String getTokenUrl,
                             @Value("${iamPort.url.cancel}") String cancelUrl) {
        this.API_KEY = apiKey;
        this.API_SECRET = secretKey;
        this.GET_TOKEN_URL = getTokenUrl;
        this.CANCEL_URL = cancelUrl;

        this.iamportClient = new IamportClient(API_KEY, API_SECRET);
    }

    public String getIamPortAuthToken() {
        WebClient webClient = WebClient.builder()
                .baseUrl(GET_TOKEN_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build()
                .mutate()
                .build();

        IamPortResponse response = webClient
                .post()
                .uri(UriBuilder::build)
                .body(BodyInserters.fromValue("{ \"imp_key\": \"" + API_KEY + "\", \"imp_secret\": \"" + API_SECRET + "\" }"))
                .retrieve()
                .bodyToMono(IamPortResponse.class)
                .block();

        assert response != null;
        return response.getResponse().getAccess_token();
    }

    public IamPortCancelResponse cancelIamPort(CancelRequest request) throws Exception {
        WebClient webClient = WebClient.builder()
                .baseUrl(CANCEL_URL)
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

        assert response != null;
        if (response.getCode() == 1) {
            throw new Exception(response.getMessage());
        }

        return response;
    }

    public IamportResponse<Payment> paymentByImpUid(String imp_uid) throws IamportResponseException, IOException {
        return iamportClient.paymentByImpUid(imp_uid);
    }
}
