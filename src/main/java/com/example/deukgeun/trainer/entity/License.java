package com.example.deukgeun.trainer.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "trainer_license")
@NoArgsConstructor
@AllArgsConstructor
public class License extends BaseEntity{
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "license_id")
  private Long id;
  
  @Column(length = 50, nullable = false)
  private String certificateName;
  
  @Column(length = 50, nullable = false)
  private String licenseNumber;
  
  @Column(name = "trainer_user_id")
  private Long trainerUserId;
  
  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "trainer_user_id", insertable = false, updatable = false, nullable = false)
  private User user;
  
  @Builder
  public License(String certificateName, String licenseNumber, Long trainerUserId) {
    this.certificateName = certificateName;
    this.trainerUserId = trainerUserId;
    this.licenseNumber = licenseNumber;
  }
}
