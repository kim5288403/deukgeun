package com.example.deukgeun.authMail.infrastructure.persistence.entity;

import com.example.deukgeun.authMail.domain.model.valueobject.MailStatus;
import com.example.deukgeun.global.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "auth_mail")
public class AuthMailEntity extends BaseEntity {
  
  @Id
  @Column(name = "auth_mail_id")
  private Long id;
  
  @Column(length = 100, nullable = false)
  private String email;

  @Column(length = 8, nullable = false)
  private String code;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private MailStatus mailStatus;
}
