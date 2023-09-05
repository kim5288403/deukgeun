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

    /**
     * IamPort API 에서 인증 토큰을 얻기 위한 메서드입니다.
     *
     * @return IamPort API 에서 얻은 인증 토큰
     */
    public String getIamPortAuthToken() {
        // WebClient 생성합니다.
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

    /**
     * IamPort 에서 결제 취소를 요청하는 메서드입니다.
     *
     * @param request 결제 취소 요청에 필요한 정보를 담은 객체
     * @return 결제 취소에 대한 응답 데이터
     * @throws Exception 결제 취소 요청이 실패한 경우 발생하는 예외
     */
    public IamPortCancelResponse cancelIamPort(CancelRequest request) throws Exception {
        // WebClient를 생성합니다.
        WebClient webClient = WebClient.builder()
                .baseUrl(CANCEL_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, request.getAccessToken())
                .build()
                .mutate()
                .build();

        // 결제 취소 요청을 준비합니다.
        IamPortCancelResponse response = webClient
                .post()
                .uri(UriBuilder::build)
                .body(BodyInserters.fromValue("{ \"imp_uid\": \"" + request.getImpUid() + "\", \"amount\": \"" + request.getAmount() + "\" }"))
                .retrieve()
                .bodyToMono(IamPortCancelResponse.class)
                .block();

        assert response != null;
        // 결제 취소가 실패한 경우 예외를 발생시킵니다.
        if (response.getCode() == 1) {
            throw new Exception(response.getMessage());
        }

        return response;
    }

    /**
     * imp_uid 를 사용하여 Iamport 에서 결제 정보를 조회하는 메서드입니다.
     *
     * @param imp_uid Iamport 에서 사용되는 고유한 결제 식별자
     * @return 결제 정보를 포함한 Iamport 응답 데이터
     * @throws IamportResponseException Iamport API 응답 처리 중 발생한 예외
     * @throws IOException 입출력 예외가 발생한 경우
     */
    public IamportResponse<Payment> paymentByImpUid(String imp_uid) throws IamportResponseException, IOException {
        return iamportClient.paymentByImpUid(imp_uid);
    }
}
