package com.example.deukgeun.trainer.domain.repository;

import com.example.deukgeun.trainer.application.dto.response.LicenseListResponse;
import com.example.deukgeun.trainer.domain.model.entity.License;

import java.util.List;

public interface LicenseRepository {
    void deleteById(Long id);
    List<LicenseListResponse> findByTrainerId(Long trainerId);
    License save(License license);
}
