package com.example.deukgeun.trainer.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "trainer_user_profile")
@NoArgsConstructor
public class Profile extends BaseEntity {

  @Id
  @Column(name = "profile_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @Column(name = "user_id")
  private Long userId;
  
  @OneToOne()
  @JoinColumn(name = "member_id", insertable = false, updatable = false, nullable = false)
  private Member member;

  @Column(length = 100, nullable = false)
  private String path;

  @Builder
  public Profile(String path, Long userId, Member member) {
    this.userId = userId;
    this.path = path;
    this.member = member;
  }
}
