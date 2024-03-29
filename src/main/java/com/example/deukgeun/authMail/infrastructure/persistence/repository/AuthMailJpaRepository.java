package com.example.deukgeun.authMail.infrastructure.persistence.repository;

import com.example.deukgeun.authMail.infrastructure.persistence.entity.AuthMailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface AuthMailJpaRepository extends JpaRepository<AuthMailEntity, Long> {
  @Transactional
  void deleteByEmail(String email);

  boolean existsByEmail(String email);

  boolean existsByEmailAndCode(String email, String code);

  Optional<AuthMailEntity> findByEmail(String email);

  Optional<AuthMailEntity> findByEmailAndCode(String email, String code);
}
