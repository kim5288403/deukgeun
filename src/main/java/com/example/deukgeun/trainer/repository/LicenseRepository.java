package com.example.deukgeun.trainer.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.example.deukgeun.trainer.entity.License;
import com.example.deukgeun.trainer.response.LicenseListResponse;
import org.springframework.data.repository.query.Param;

public interface LicenseRepository extends JpaRepository<License, Long>{
  List<LicenseListResponse> findByMemberId(Long memberId);
}
