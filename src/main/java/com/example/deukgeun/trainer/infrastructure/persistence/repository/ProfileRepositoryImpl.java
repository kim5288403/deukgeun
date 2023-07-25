package com.example.deukgeun.trainer.infrastructure.persistence.repository;

import com.example.deukgeun.trainer.infrastructure.persistence.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepositoryImpl extends JpaRepository<ProfileEntity, Long> {
  Optional<ProfileEntity> findByTrainerId(Long trainerId);
  
}
