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
@NoArgsConstructor
@Table(name = "trainer_user_profile")
public class Profile extends BaseEntity {

  @Id
  @Column(name = "profile_id", unique = true, nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 100, nullable = false)
  private String path;

  @Builder
  public Profile(String path) {
    this.path = path;
  }
}
