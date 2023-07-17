package com.example.deukgeun.trainer.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.example.deukgeun.global.entity.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "trainer_post")
@NoArgsConstructor
public class Post extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "post_id")
  private Long id;
  
  @Column(name = "trainer_id")
  private Long trainerId;
  
  @Column(nullable = false)
  private String html;
  
  @OneToOne
  @JoinColumn(name = "trainer_id", insertable = false, updatable = false, nullable = false)
  private Trainer trainer;
  
  @Builder
  public Post(Long id, String html, Long trainerId) {
    this.id = id;
    this.html = html;
    this.trainerId = trainerId;
  }

  public void updateHtml(String html) {
    this.html = html;
  }
}
