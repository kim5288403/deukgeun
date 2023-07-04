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
  
  @Column(name = "member_id")
  private Long memberId;
  
  @OneToOne()
  @JoinColumn(name = "member_id", insertable = false, updatable = false, nullable = false)
  private Member member;

  @Column(length = 100, nullable = false)
  private String path;

  @Builder
  public Profile(Long id, String path, Long memberId, Member member) {
    this.id = id;
    this.memberId = memberId;
    this.path = path;
    this.member = member;
  }

  public void updatePath(String path) {
    this.path = path;
  }
}
