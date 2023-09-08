package com.example.deukgeun.trainer.domain.service;

import com.example.deukgeun.trainer.application.dto.request.JoinRequest;
import com.example.deukgeun.trainer.application.dto.request.UpdateInfoRequest;
import com.example.deukgeun.trainer.application.dto.request.UpdatePasswordRequest;
import com.example.deukgeun.trainer.application.dto.response.LicenseResponse;
import com.example.deukgeun.trainer.domain.model.aggregate.Trainer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.persistence.EntityNotFoundException;

public interface TrainerDomainService {
    void deleteById(Long id);
    void deleteLicenseByLicenseId(String email, Long licenseId);
    void deletePost(String email);
    boolean existsByEmail(String email);
    Trainer findById(Long id) throws EntityNotFoundException;
    Trainer findByEmail(String email) throws EntityNotFoundException;
    UserDetails loadUserByTrainerUsername(String email) throws UsernameNotFoundException;
    Trainer save(JoinRequest request, String fileName);
    Trainer saveLicense(String email, LicenseResponse.Result licenseResult);
    void updateInfoByEmail(UpdateInfoRequest request);
    String updateProfileByEmail(String email, String fileName);
    void updatePasswordByEmail(UpdatePasswordRequest request);
    void uploadPostByEmail(String email, String html);
}
