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
  
  @Column(name = "user_id")
  private Long userId;
  
  @Column(nullable = false)
  private String html;
  
  @OneToOne()
  @JoinColumn(name = "user_id", insertable = false, updatable = false, nullable = false)
  private User user;
  
  @Builder
  public Post(String html, Long userId) {
    this.html = html;
    this.userId = userId;
  }
}
