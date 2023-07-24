package com.example.deukgeun.trainer.domain.service.implement;

import com.example.deukgeun.trainer.application.dto.response.LicenseListResponse;
import com.example.deukgeun.trainer.application.dto.response.LicenseResultResponse;
import com.example.deukgeun.trainer.domain.model.entity.License;
import com.example.deukgeun.trainer.domain.repository.LicenseRepository;
import com.example.deukgeun.trainer.domain.service.LicenseDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LicenseDomainServiceImpl implements LicenseDomainService {
    private final LicenseRepository licenseRepository;

    @CacheEvict(value = "license", key = "#id", cacheManager = "projectCacheManager")
    public void deleteById(Long id) {
        licenseRepository.deleteById(id);
    }

    @Cacheable(value = "license", key = "#userId", cacheManager = "projectCacheManager")
    public List<LicenseListResponse> findByTrainerId(Long trainerId) {
        return licenseRepository.findByTrainerId(trainerId);
    }
    public License save(LicenseResultResponse licenseResult, Long trainerId) {
        License license = License.create(licenseResult.getCertificatename(), licenseResult.getNo(), trainerId);

        licenseRepository.save(license);
        return license;
    }
}
