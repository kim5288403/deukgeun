package com.example.deukgeun.trainer.application.service;

import com.example.deukgeun.trainer.application.dto.request.JoinRequest;
import com.example.deukgeun.trainer.application.dto.request.UpdateInfoRequest;
import com.example.deukgeun.trainer.application.dto.request.UpdatePasswordRequest;
import com.example.deukgeun.trainer.domain.model.entity.Trainer;

import javax.persistence.EntityNotFoundException;

public interface TrainerApplicationService {
    void deleteById(Long id);
    boolean existsByEmail(String email);
    Trainer findByEmail(String email) throws EntityNotFoundException;
    boolean isEmptyGroupName(String groupName, String groupStatus);
    Trainer save(JoinRequest request);
    void updateInfo(UpdateInfoRequest request);
    void updatePassword(UpdatePasswordRequest request);
}
