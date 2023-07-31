package com.example.deukgeun.trainer.application.service;

import com.example.deukgeun.trainer.application.dto.request.JoinRequest;
import com.example.deukgeun.trainer.application.dto.request.PostRequest;
import com.example.deukgeun.trainer.application.dto.request.UpdateInfoRequest;
import com.example.deukgeun.trainer.application.dto.request.UpdatePasswordRequest;
import com.example.deukgeun.trainer.application.dto.response.LicenseResultResponse;
import com.example.deukgeun.trainer.domain.model.aggregate.Trainer;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public interface TrainerApplicationService {
    void delete(String email) throws IOException;
    void deleteLicenseByLicenseId(String email, Long licenseId);
    void deletePost(String email);
    void deleteImageToServer(String email) throws IOException;
    boolean existsByEmail(String email);
    Trainer findById(Long id);
    Trainer findByEmail(String src);
    File getServerImage(String url);
    boolean isEmptyGroupName(String groupName, String groupStatus);
    Trainer save(JoinRequest request) throws IOException;
    Trainer saveLicense(String email, LicenseResultResponse licenseResult);
    Map<Object, Object> saveImageToServer(HttpServletRequest request, HttpServletResponse response) throws Exception;
    void updateInfo(UpdateInfoRequest request);
    void updateProfile(String email, MultipartFile profile) throws IOException;
    void updatePassword(UpdatePasswordRequest request);
    void uploadPost(String email, PostRequest postRequest);
}
