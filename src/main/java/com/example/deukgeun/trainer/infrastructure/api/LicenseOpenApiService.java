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
    private String LICENSE_API_KEY;
    @Value("${trainer.license.api.uri}")
    private String LICENSE_API_URI;

    /**
     * 주어진 자격증 정보를 사용하여 자격증 검증 API를 호출하고 결과를 반환합니다.
     *
     * @param request 자격증 정보를 담은 요청 객체
     * @return 자격증 검증 결과를 담은 객체
     * @throws IllegalArgumentException 주어진 자격증 정보가 존재하지 않거나 검증에 실패한 경우 발생
     */
    public LicenseResponse.Result getLicenseVerificationResult(SaveLicenseRequest request) {
        // WebClient를 생성하여 자격증 검증 API에 요청을 보냅니다.
        WebClient webClient = WebClient.builder()
                .baseUrl(LICENSE_API_URI)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build()
                .mutate()
                .build();

        // API 호출을 통해 자격증 검증 결과를 가져옵니다.
        LicenseResponse.Result result = webClient.get().
                uri(uriBuilder -> uriBuilder
                        .path("")
                        .queryParam("apiKey", LICENSE_API_KEY)
                        .queryParam("name", request.getCertificateName())
                        .queryParam("no", request.getNo())
                        .build())
                .retrieve()
                .bodyToMono(LicenseResponse.Result.class)
                .block();

        // 검증 결과가 null이거나 검증에 실패한 경우 예외를 발생시킵니다.
        if (result == null || !result.getResult()) {
            throw new IllegalArgumentException("존재하지않는 자격증 정보 입니다.");
        }

        return result;
    }
}
