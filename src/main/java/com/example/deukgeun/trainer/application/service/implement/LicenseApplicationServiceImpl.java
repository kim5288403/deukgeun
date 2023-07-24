package com.example.deukgeun.trainer.application.service.implement;

import com.example.deukgeun.trainer.application.dto.request.SaveLicenseRequest;
import com.example.deukgeun.trainer.application.dto.response.LicenseListResponse;
import com.example.deukgeun.trainer.application.dto.response.LicenseResultResponse;
import com.example.deukgeun.trainer.application.service.LicenseApplicationService;
import com.example.deukgeun.trainer.domain.model.entity.License;
import com.example.deukgeun.trainer.domain.service.LicenseDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@RequiredArgsConstructor
@Service
public class LicenseApplicationServiceImpl implements LicenseApplicationService {

    private final LicenseDomainService licenseDomainService;

    @Value("${trainer.license.api.key}")
    private String licenseApiKey;
    @Value("${trainer.license.api.uri}")
    private String licenseApiUri;

    @Cacheable(value = "license", key = "#userId", cacheManager = "projectCacheManager")
    public List<LicenseListResponse> findByTrainerId(Long trainerId) {
        return licenseDomainService.findByTrainerId(trainerId);
    }

    public License save(LicenseResultResponse licenseResult, Long trainerId) throws Exception {
        return licenseDomainService.save(licenseResult, trainerId);
    }

    @CacheEvict(value = "license", key = "#id", cacheManager = "projectCacheManager")
    public void deleteById(Long id) {
        licenseDomainService.deleteById(id);
    }

    public LicenseResultResponse getLicenseVerificationResult(SaveLicenseRequest request) {
        // 라이선스 API와 통신하기 위한 WebClient 생성
        WebClient webClient = WebClient.builder()
                .baseUrl(licenseApiUri)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build()
                .mutate()
                .build();

        // 라이선스 확인 요청을 위한 URI 생성 및 API 호출
        return webClient.get().
                uri(uriBuilder -> uriBuilder
                        .path("")
                        .queryParam("apiKey", licenseApiKey)
                        .queryParam("name", request.getName())
                        .queryParam("no", request.getNo())
                        .build())
                .retrieve()
                .bodyToMono(LicenseResultResponse.class)
                .block();
    }

    public void checkLicense(LicenseResultResponse result) {
        if (result == null || !result.getResult()) {
            throw new IllegalArgumentException("존재하지않는 자격증 정보 입니다.");
        }
    }
}
