package com.example.deukgeun.commom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import com.example.deukgeun.commom.entity.AuthMail;
import com.example.deukgeun.commom.enums.MailStatus;

import java.util.Optional;

public interface AuthMailRepository extends JpaRepository<AuthMail, Long>{
  boolean existsByEmailAndCode(String email, String code);

  boolean existsByEmail(String email);

  Optional<AuthMail> findByEmail(String email);
  
  @Transactional
  void deleteByEmail(String email);
}
