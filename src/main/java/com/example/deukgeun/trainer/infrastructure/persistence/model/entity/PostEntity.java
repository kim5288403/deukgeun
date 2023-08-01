package com.example.deukgeun.trainer.infrastructure.persistence.model.entity;

import com.example.deukgeun.global.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "trainer_post")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostEntity extends BaseEntity {

  @Id
  @Column(name = "post_id")
  private Long id;

  @Column(nullable = false)
  private String html;
}
