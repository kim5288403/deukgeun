package com.example.deukgeun.trainer.application.service;

import com.example.deukgeun.trainer.application.dto.response.ProfileResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ProfileApplicationService {
    ProfileResponse getProfileId(Long id);
    ProfileResponse getProfileByEmail(String email);
    void updateProfile(String email, MultipartFile file) throws Exception;
}
