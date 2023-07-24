package com.example.deukgeun.trainer.infrastructure.persistence.entity;

import com.example.deukgeun.global.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "trainer_license")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LicenseEntity extends BaseEntity {
  
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
  private TrainerEntity trainer;
}
