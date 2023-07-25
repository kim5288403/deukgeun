package com.example.deukgeun.trainer.application.service;

import com.example.deukgeun.trainer.domain.model.entity.Profile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProfileApplicationService {
    void deleteById(Long profileId);
    void deleteFileToDirectory(String fileName)throws IOException;
    String getUUIDPath(String fileName) throws IOException;
    Profile findById(Long profileId);
    Profile findByTrainerId(Long trainerId);
    boolean isSupportedContentType(MultipartFile file);
    void save(MultipartFile profile, Long trainerId) throws IOException;
    void saveFileToDirectory(MultipartFile profile, String fileName) throws IOException;
    void update(Profile foundProfile, String path);
    void updateProfile(MultipartFile profile, Long profileId) throws Exception;

}
