package com.example.deukgeun.trainer.application.service;

import com.example.deukgeun.trainer.application.dto.request.JoinRequest;
import com.example.deukgeun.trainer.application.dto.request.PostRequest;
import com.example.deukgeun.trainer.application.dto.request.UpdateInfoRequest;
import com.example.deukgeun.trainer.application.dto.request.UpdatePasswordRequest;
import com.example.deukgeun.trainer.application.dto.response.LicenseResponse;
import com.example.deukgeun.trainer.domain.model.aggregate.Trainer;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
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
    List<LicenseResponse.List> getLicensesById(Long id);
    List<LicenseResponse.List> getLicensesByEmail(String email);
    boolean isEmptyGroupName(String groupName, String groupStatus);
    Trainer save(JoinRequest request) throws IOException;
    Trainer saveLicense(String email, LicenseResponse.Result licenseResult);
    Map<Object, Object> saveImageToServer(HttpServletRequest request, HttpServletResponse response) throws Exception;
    void updateInfo(UpdateInfoRequest request);
    void updateProfile(String email, MultipartFile profile) throws IOException;
    void updatePassword(UpdatePasswordRequest request);
    void uploadPost(String email, PostRequest postRequest);
}
