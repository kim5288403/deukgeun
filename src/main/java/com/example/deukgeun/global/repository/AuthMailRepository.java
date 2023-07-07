package com.example.deukgeun.global.repository;

import com.example.deukgeun.global.entity.AuthMail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface AuthMailRepository extends JpaRepository<AuthMail, Long>{
  boolean existsByEmailAndCode(String email, String code);

  boolean existsByEmail(String email);

  Optional<AuthMail> findByEmail(String email);
  
  @Transactional
  void deleteByEmail(String email);
}
