package com.example.deukgeun.trainer.infrastructure.api;

import com.example.deukgeun.trainer.application.dto.request.SaveLicenseRequest;
import com.example.deukgeun.trainer.application.dto.response.LicenseResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class LicenseOpenApiService {
    @Value("${trainer.license.api.key}")
    private String licenseApiKey;
    @Value("${trainer.license.api.uri}")
    private String licenseApiUri;

    public LicenseResponse.Result getLicenseVerificationResult(SaveLicenseRequest request) {
        // 라이선스 API와 통신하기 위한 WebClient 생성
        WebClient webClient = WebClient.builder()
                .baseUrl(licenseApiUri)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build()
                .mutate()
                .build();

        // 라이선스 확인 요청을 위한 URI 생성 및 API 호출
        LicenseResponse.Result result = webClient.get().
                uri(uriBuilder -> uriBuilder
                        .path("")
                        .queryParam("apiKey", licenseApiKey)
                        .queryParam("name", request.getCertificateName())
                        .queryParam("no", request.getNo())
                        .build())
                .retrieve()
                .bodyToMono(LicenseResponse.Result.class)
                .block();

        if (result == null || !result.getResult()) {
            throw new IllegalArgumentException("존재하지않는 자격증 정보 입니다.");
        }

        return result;
    }
}
