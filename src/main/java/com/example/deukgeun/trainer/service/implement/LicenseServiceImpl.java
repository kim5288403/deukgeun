package com.example.deukgeun.trainer.service.implement;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.example.deukgeun.commom.util.WebClientUtil;
import com.example.deukgeun.trainer.entity.License;
import com.example.deukgeun.trainer.repository.LicenseRepository;
import com.example.deukgeun.trainer.request.SaveLicenseRequest;
import com.example.deukgeun.trainer.response.LicenseListResponse;
import com.example.deukgeun.trainer.response.LicenseResultResponse;
import com.example.deukgeun.trainer.service.LicenseService;
import lombok.RequiredArgsConstructor;

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
     * @param userId 사용자 ID
     * @return 라이선스 목록
     */
    @Cacheable(value = "license", key = "#userId", cacheManager = "projectCacheManager")
    public List<LicenseListResponse> findByUserId(Long userId) {
        return licenseRepository.findByUserId(userId);
    }

    /**
     * 라이선스를 저장합니다.
     *
     * @param licenseResult 라이선스 진위여부 결과 응답 객체
     * @param userId        사용자 ID
     * @return 저장된 라이선스 객체
     * @throws Exception 저장 중 발생한 예외
     */
    public License save(LicenseResultResponse licenseResult, Long userId) throws Exception {
        License license = SaveLicenseRequest.create(licenseResult.getCertificatename(), licenseResult.getNo(), userId);
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
     * 라이선스 진위여부를 확인합니다.
     *
     * @param request 라이선스 저장 요청 객체
     * @return 라이선스 결과 응답 객체
     * @throws IllegalArgumentException 자격증 정보가 존재하지 않을 경우 발생하는 예외
     */
    public LicenseResultResponse checkLicense(SaveLicenseRequest request) {
        // 라이선스 API와 통신하기 위한 WebClient 생성
        WebClient webClient = WebClientUtil.getBaseUrl(licenseApiUri);

        // 라이선스 확인 요청을 위한 URI 생성 및 API 호출
        LicenseResultResponse result = webClient.get().
                uri(uriBuilder -> uriBuilder
                        .path("")
                        .queryParam("apiKey", licenseApiKey)
                        .queryParam("name", request.getName())
                        .queryParam("no", request.getNo())
                        .build())
                .retrieve()
                .bodyToMono(LicenseResultResponse.class)
                .block();

        // API 응답 결과 검증
        assert result != null;
        if (result.getResult()) {
            result.setNo(request.getNo());
            return result;
        } else {
            throw new IllegalArgumentException("존재하지않는 자격증 정보 입니다.");
        }
    }


}
