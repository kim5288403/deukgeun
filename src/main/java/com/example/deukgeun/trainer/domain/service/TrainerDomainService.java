package com.example.deukgeun.trainer.domain.service;

import com.example.deukgeun.trainer.domain.dto.SaveLicenseDTO;
import com.example.deukgeun.trainer.domain.dto.SaveTrainerDTO;
import com.example.deukgeun.trainer.domain.dto.UpdateInfoDTO;
import com.example.deukgeun.trainer.domain.dto.UpdatePasswordDTO;
import com.example.deukgeun.trainer.domain.model.aggregate.Trainer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.persistence.EntityNotFoundException;

public interface TrainerDomainService {
    void deleteById(Long id);
    void deleteLicenseByEmailAndLicenseId(String email, Long licenseId);
    void deletePostByEmail(String email);
    boolean existsByEmail(String email);
    Trainer findById(Long id) throws EntityNotFoundException;
    Trainer findByEmail(String email) throws EntityNotFoundException;
    UserDetails loadUserByTrainerUsername(String email) throws UsernameNotFoundException;
    Trainer save(SaveTrainerDTO saveTrainerDTO);
    Trainer saveLicense(SaveLicenseDTO saveLicenseDTO);
    void updateInfoByEmail(UpdateInfoDTO updateInfoDTO);
    String updateProfileByEmail(String email, String fileName);
    void updatePasswordByEmail(UpdatePasswordDTO updatePasswordDTO);
    void uploadPostByEmail(String email, String html);
}
