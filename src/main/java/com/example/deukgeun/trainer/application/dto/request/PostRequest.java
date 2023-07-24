package com.example.deukgeun.trainer.application.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PostRequest {
  
  @NotBlank(message = "필수 입력 값입니다.")
  private String content;
}
