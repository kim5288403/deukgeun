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
@Table(name = "trainer_post")
@NoArgsConstructor
public class Post extends BaseEntity{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "post_id")
  private Long id;
  
  @Column(name = "member_id")
  private Long memberId;
  
  @Column(nullable = false)
  private String html;
  
  @OneToOne()
  @JoinColumn(name = "member_id", insertable = false, updatable = false, nullable = false)
  private Member member;
  
  @Builder
  public Post(Long id, String html, Long memberId) {
    this.id = id;
    this.html = html;
    this.memberId = memberId;
  }

  public void updateHtml(String html) {
    this.html = html;
  }
}
