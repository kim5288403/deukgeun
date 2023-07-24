package com.example.deukgeun.trainer.infrastructure.persistence.repository;

import com.example.deukgeun.trainer.infrastructure.persistence.entity.LicenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LicenseRepositoryImpl extends JpaRepository<LicenseEntity, Long>{
  List<LicenseEntity> findByTrainerId(Long trainerId);
}
