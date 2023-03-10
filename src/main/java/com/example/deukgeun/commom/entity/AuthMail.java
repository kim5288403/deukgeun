package com.example.deukgeun.commom.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import com.example.deukgeun.commom.enums.MailStatus;
import com.example.deukgeun.trainer.entity.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "auth_mail")
public class AuthMail extends BaseEntity{
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "mail_id")
  private Long id;
  
  @Column(length = 100, nullable = false)
  private String email;
  
  @Column(length = 8, nullable = false)
  private String code;
  
  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private MailStatus status;
  
  @Builder
  public AuthMail(String email, String code, MailStatus status) {
    this.email = email;
    this.code = code;
    this.status = status;
  }
  
}
