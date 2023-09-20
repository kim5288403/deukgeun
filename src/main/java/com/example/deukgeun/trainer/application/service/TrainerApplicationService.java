package com.example.deukgeun.trainer.application.service;

import com.example.deukgeun.trainer.application.dto.request.JoinRequest;
import com.example.deukgeun.trainer.application.dto.request.PostRequest;
import com.example.deukgeun.trainer.application.dto.request.UpdateInfoRequest;
import com.example.deukgeun.trainer.application.dto.request.UpdatePasswordRequest;
import com.example.deukgeun.trainer.application.dto.response.LicenseResponse;
import com.example.deukgeun.trainer.application.dto.response.ProfileResponse;
import com.example.deukgeun.trainer.application.dto.response.TrainerResponse;
import com.example.deukgeun.trainer.domain.model.aggregate.Trainer;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface TrainerApplicationService {
    void delete(String email) throws IOException;
    boolean existsByEmail(String email);
    Trainer findById(Long id);
    Trainer findByEmail(String src);
    TrainerResponse.Info getInfoByEmail(String email);
    TrainerResponse.Detail getDetailByEmail(String email);
    Trainer save(JoinRequest request) throws IOException;
    void updateInfo(UpdateInfoRequest request);
    void updatePassword(UpdatePasswordRequest request);
}
