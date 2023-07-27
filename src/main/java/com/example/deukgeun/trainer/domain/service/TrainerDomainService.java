package com.example.deukgeun.trainer.domain.service;

import com.example.deukgeun.trainer.application.dto.request.JoinRequest;
import com.example.deukgeun.trainer.application.dto.request.UpdateInfoRequest;
import com.example.deukgeun.trainer.application.dto.request.UpdatePasswordRequest;
import com.example.deukgeun.trainer.application.dto.response.LicenseResultResponse;
import com.example.deukgeun.trainer.domain.model.aggregate.Trainer;

import javax.persistence.EntityNotFoundException;

public interface TrainerDomainService {
    void deleteById(Long id);
    void deleteLicenseByLicenseId(String email, Long licenseId);
    boolean existsByEmail(String email);
    Trainer findById(Long id) throws EntityNotFoundException;
    Trainer findByEmail(String email) throws EntityNotFoundException;
    Trainer save(JoinRequest request);
    Trainer saveLicense(String email, LicenseResultResponse licenseResult);
    void updateInfo(UpdateInfoRequest request);
    void updatePassword(UpdatePasswordRequest request);
}
