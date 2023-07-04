package com.example.deukgeun.trainer.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
  
  @Column(name = "trainer_id")
  private Long trainerId;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "trainer_id", insertable = false, updatable = false, nullable = false)
  private Trainer trainer;
  
  @Builder
  public License(String certificateName, String licenseNumber, Long trainerId) {
    this.certificateName = certificateName;
    this.trainerId = trainerId;
    this.licenseNumber = licenseNumber;
  }
}
