package com.example.deukgeun.commom.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.deukgeun.trainer.entity.License;
import com.example.deukgeun.trainer.response.LicenseListResponse;

public interface LicenseRepository extends JpaRepository<License, Long> {
  List<LicenseListResponse> findByUserId(Long userId);
}
