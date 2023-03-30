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
@Table(name = "post_image")
@Getter
@NoArgsConstructor
public class PostImage extends BaseEntity{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "post_image_id")
  private Long id;
  
  @Column(name = "user_id")
  private Long user_id;
  
  @Column(length = 100, nullable = false)
  private String path;
  
  @Builder
  public PostImage(Long user_id, String path) {
    this.user_id = user_id;
    this.path = path;
  }
}
