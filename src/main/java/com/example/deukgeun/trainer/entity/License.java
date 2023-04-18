package com.example.deukgeun.trainer.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "trainer_license")
@NoArgsConstructor
public class License extends BaseEntity{
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "license_id")
  private Long id;
  
  @Column(length = 50, nullable = false)
  private String certificateName;
  
  @Column(name = "trainer_user_id")
  private Long trainerUserId;
  
  @Builder
  public License(String certificateName, Long trainerUserId) {
    this.certificateName = certificateName;
    this.trainerUserId = trainerUserId;
  }
}
