package com.example.deukgeun.trainer.repository;

import com.example.deukgeun.trainer.entity.License;
import com.example.deukgeun.trainer.response.LicenseListResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LicenseRepository extends JpaRepository<License, Long>{
  List<LicenseListResponse> findByTrainerId(Long trainerId);
}
