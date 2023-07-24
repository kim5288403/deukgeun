package com.example.deukgeun.trainer.domain.service;

import com.example.deukgeun.trainer.application.dto.response.LicenseListResponse;
import com.example.deukgeun.trainer.application.dto.response.LicenseResultResponse;
import com.example.deukgeun.trainer.domain.model.entity.License;

import java.util.List;

public interface LicenseDomainService {
    void deleteById(Long id);
    List<LicenseListResponse> findByTrainerId(Long trainerId);
    License save(LicenseResultResponse licenseResult, Long trainerId);
}
