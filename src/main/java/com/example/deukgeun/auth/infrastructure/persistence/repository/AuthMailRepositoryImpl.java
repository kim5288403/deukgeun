package com.example.deukgeun.auth.infrastructure.persistence.repository;

import com.example.deukgeun.auth.infrastructure.persistence.entity.AuthMailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface AuthMailRepositoryImpl extends JpaRepository<AuthMailEntity, Long> {
  boolean existsByEmailAndCode(String email, String code);

  boolean existsByEmail(String email);

  Optional<AuthMailEntity> findByEmail(String email);
  
  @Transactional
  void deleteByEmail(String email);
}
