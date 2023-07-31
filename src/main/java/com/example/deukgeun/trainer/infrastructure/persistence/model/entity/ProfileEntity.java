package com.example.deukgeun.trainer.infrastructure.persistence.model.entity;

import com.example.deukgeun.global.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "trainer_profile")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileEntity extends BaseEntity {

  @Id
  @Column(name = "profile_id")
  private Long id;
  
  @Column(name = "trainer_id")
  private Long trainerId;

  @Column(length = 100, nullable = false)
  private String path;
}
