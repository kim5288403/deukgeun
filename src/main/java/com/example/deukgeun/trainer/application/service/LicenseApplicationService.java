package com.example.deukgeun.trainer.application.service;

import com.example.deukgeun.trainer.application.dto.request.SaveLicenseRequest;
import com.example.deukgeun.trainer.application.dto.response.LicenseListResponse;
import com.example.deukgeun.trainer.application.dto.response.LicenseResultResponse;
import com.example.deukgeun.trainer.domain.model.entity.License;

import java.util.List;

public interface LicenseApplicationService {
    void checkLicense(LicenseResultResponse result);
    void deleteById(Long id);
    List<LicenseListResponse> findByTrainerId(Long trainerId);
    LicenseResultResponse getLicenseVerificationResult(SaveLicenseRequest request);
    License save(LicenseResultResponse licenseResult, Long trainerId) throws Exception;
}
