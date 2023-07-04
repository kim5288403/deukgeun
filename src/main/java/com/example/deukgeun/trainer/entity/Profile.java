package com.example.deukgeun.trainer.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "trainer_profile")
@NoArgsConstructor
public class Profile extends BaseEntity {

  @Id
  @Column(name = "profile_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @Column(name = "trainer_id")
  private Long trainerId;
  
  @OneToOne()
  @JoinColumn(name = "trainer_id", insertable = false, updatable = false, nullable = false)
  private Trainer trainer;

  @Column(length = 100, nullable = false)
  private String path;

  @Builder
  public Profile(Long id, String path, Long trainerId, Trainer trainer) {
    this.id = id;
    this.trainerId = trainerId;
    this.path = path;
    this.trainer = trainer;
  }

  public void updatePath(String path) {
    this.path = path;
  }
}
