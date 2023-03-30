package com.example.deukgeun.trainer.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import com.example.deukgeun.trainer.entity.PostImage;
import lombok.Data;

@Data
public class PostImageRequest {
  
  @NotNull(message = "유저 아이디는 필수 입력 값입니다.")
  private Long user_id;
  
  @NotBlank(message = "이미지 경로는 필수 입력 값입니다.")
  private String path;
  
  public static PostImage create(Long user_id, String path) {
    return PostImage
        .builder()
        .user_id(user_id)
        .path(path)
        .build();
  }
}
