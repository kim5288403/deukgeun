package com.example.deukgeun.trainer.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.example.deukgeun.trainer.entity.License;
import com.example.deukgeun.trainer.response.LicenseListResponse;

public interface LicenseRepository extends JpaRepository<License, Long>{
  List<LicenseListResponse> findByUserId(Long userId);
  
  @Query("select DISTINCT l from License l join fetch l.user")
  List<License> findLicenseFetchJoin();
}
