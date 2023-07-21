package com.example.deukgeun.trainer.infrastructure.persistence.repository;

import com.example.deukgeun.trainer.infrastructure.persistence.entity.License;
import com.example.deukgeun.trainer.application.dto.response.LicenseListResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LicenseRepository extends JpaRepository<License, Long>{
  List<LicenseListResponse> findByTrainerId(Long trainerId);
}
