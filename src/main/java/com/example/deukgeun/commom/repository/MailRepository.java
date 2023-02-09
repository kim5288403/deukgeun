package com.example.deukgeun.commom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import com.example.deukgeun.commom.entity.AuthMail;
import com.example.deukgeun.commom.enums.MailStatus;

public interface MailRepository extends JpaRepository<AuthMail, Long>{
  boolean existsByEmailAndCode(String email, String code);
  
  @Modifying
  @Transactional
  @Query("update AuthMail m set m.status = :status where m.email = :email and m.code = :code")
  int updateStatusByEmailAndCode(@Param(value = "email") String email, @Param(value = "code") String code, @Param(value = "status") MailStatus status);
}
