package com.example.deukgeun.trainer.infrastructure.persistence.entity;

import com.example.deukgeun.global.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@Table(name = "trainer_post")
@AllArgsConstructor
@NoArgsConstructor
public class PostEntity extends BaseEntity {
  @Id
  @Column(name = "post_id")
  private Long id;

  @Column(name = "trainer_id")
  private Long trainerId;

  @Column(nullable = false)
  private String html;

  @OneToOne
  @JoinColumn(name = "trainer_id", insertable = false, updatable = false, nullable = false)
  private TrainerEntity trainer;
}
