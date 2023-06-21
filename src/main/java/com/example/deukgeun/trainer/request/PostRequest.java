package com.example.deukgeun.trainer.request;

import javax.validation.constraints.NotBlank;
import com.example.deukgeun.trainer.entity.Post;
import lombok.Data;

@Data
public class PostRequest {
  
  @NotBlank(message = "필수 입력 값입니다.")
  private String content;
  
  public static Post create(String html, Long memberId) {
    return Post.builder()
        .memberId(memberId)
        .html(html)
        .build();
  }
}
