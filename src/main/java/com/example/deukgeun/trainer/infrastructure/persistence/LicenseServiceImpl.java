package com.example.deukgeun.trainer.infrastructure.persistence;

import com.example.deukgeun.trainer.domain.entity.License;
import com.example.deukgeun.trainer.domain.repository.LicenseRepository;
import com.example.deukgeun.trainer.application.dto.request.SaveLicenseRequest;
import com.example.deukgeun.trainer.application.dto.response.LicenseListResponse;
import com.example.deukgeun.trainer.application.dto.response.LicenseResultResponse;
import com.example.deukgeun.trainer.domain.service.LicenseService;
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
public class LicenseServiceImpl implements LicenseService {

    private final LicenseRepository licenseRepository;

    @Value("${trainer.license.api.key}")
    private String licenseApiKey;
    @Value("${trainer.license.api.uri}")
    private String licenseApiUri;

    /**
     * 사용자 ID에 해당하는 라이선스 목록을 조회합니다.
     *
     * @param trainerId 사용자 ID
     * @return 라이선스 목록
     */
    @Cacheable(value = "license", key = "#userId", cacheManager = "projectCacheManager")
    public List<LicenseListResponse> findByTrainerId(Long trainerId) {
        return licenseRepository.findByTrainerId(trainerId);
    }

    /**
     * 라이선스를 저장합니다.
     *
     * @param licenseResult 라이선스 진위여부 결과 응답 객체
     * @param trainerId      사용자 ID
     * @return 저장된 라이선스 객체
     * @throws Exception 저장 중 발생한 예외
     */
    public License save(LicenseResultResponse licenseResult, Long trainerId) throws Exception {
        License license = License.builder()
                .certificateName(licenseResult.getCertificatename())
                .trainerId(trainerId)
                .licenseNumber(licenseResult.getNo())
                .build();

        licenseRepository.save(license);
        return license;
    }

    /**
     * ID를 사용하여 라이선스를 삭제합니다.
     *
     * @param id 삭제할 라이선스의 ID
     */
    @CacheEvict(value = "license", key = "#id", cacheManager = "projectCacheManager")
    public void delete(Long id) {
        licenseRepository.deleteById(id);
    }

    /**
     * 자격증 검증 결과를 가져오는 메소드입니다.
     *
     * @param request 자격증 요청 정보
     * @return 자격증 검증 결과
     */
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


    /**
     * 자격증 검증 결과를 확인하는 메소드입니다.
     *
     * @param result 자격증 검증 결과
     * @throws IllegalArgumentException 자격증 정보가 존재하지 않거나 유효하지 않을 경우 발생하는 예외
     */
    public void checkLicense(LicenseResultResponse result) {
        if (result == null || !result.getResult()) {
            throw new IllegalArgumentException("존재하지않는 자격증 정보 입니다.");
        }
    }
}
