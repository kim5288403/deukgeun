package com.example.deukgeun.payment.service.implement;

import com.example.deukgeun.global.entity.PaymentInfo;
import com.example.deukgeun.global.repository.PaymentInfoRepository;
import com.example.deukgeun.payment.request.CancelRequest;
import com.example.deukgeun.payment.request.PaymentInfoRequest;
import com.example.deukgeun.payment.response.IamPortCancelResponse;
import com.example.deukgeun.payment.response.IamPortResponse;
import com.example.deukgeun.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentInfoRepository paymentInfoRepository;

    @Override
    public PaymentInfo save(PaymentInfoRequest request) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        PaymentInfo paymentInfo = PaymentInfo
                .builder()
                .applicantId(request.getApplicantId())
                .impUid(request.getImpUid())
                .pgProvider(request.getPgProvider())
                .pgTid(request.getPgTid())
                .channel(request.getChannel())
                .amount(request.getAmount())
                .paidAt(LocalDateTime.parse(request.getPaidAt(), formatter))
                .build();

        return paymentInfoRepository.save(paymentInfo);
    }

    @Override
    public PaymentInfo getPaymentInfoByApplicantId(Long applicantId) {
        return paymentInfoRepository.findByApplicantId(applicantId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지않는 결제 정보입니다."));
    }

    @Override
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

    @Override
    public IamPortCancelResponse cancelIamPort(CancelRequest request) {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://api.iamport.kr/payments/cancel")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, request.getAccessToken())
                .build()
                .mutate()
                .build();

        return webClient
                .post()
                .uri(UriBuilder::build)
                .body(BodyInserters.fromValue("{ \"imp_uid\": \"" + request.getImpUid() + "\", \"amount\": \"" + request.getAmount() + "\" }"))
                .retrieve()
                .bodyToMono(IamPortCancelResponse.class)
                .block();
    }
}
