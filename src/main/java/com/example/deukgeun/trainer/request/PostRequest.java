package com.example.deukgeun.trainer.request;

import com.example.deukgeun.global.entity.Post;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PostRequest {
  
  @NotBlank(message = "필수 입력 값입니다.")
  private String content;
  
  public static Post create(String html, Long trainerId) {
    return Post.builder()
        .trainerId(trainerId)
        .html(html)
        .build();
  }
}
