package com.example.deukgeun.trainer.infrastructure.persistence.repository;


import com.example.deukgeun.trainer.infrastructure.persistence.model.entity.TrainerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainerJpaRepository extends JpaRepository<TrainerEntity, Long> {
  boolean existsByEmail(String email);
  Optional<TrainerEntity> findByEmail(String email);
}
