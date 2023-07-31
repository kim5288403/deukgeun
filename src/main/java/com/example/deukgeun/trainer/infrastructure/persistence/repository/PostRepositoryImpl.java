package com.example.deukgeun.trainer.infrastructure.persistence.repository;

import com.example.deukgeun.trainer.infrastructure.persistence.model.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepositoryImpl extends JpaRepository<PostEntity, Long>{
  Optional<PostEntity> findByTrainerId(Long trainerId);
  boolean existsByTrainerId(Long trainerId);
}
