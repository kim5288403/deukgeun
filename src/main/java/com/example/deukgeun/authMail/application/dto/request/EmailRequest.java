package com.example.deukgeun.authMail.application.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class EmailRequest {
  @NotBlank(message = "email 은 필수 입력 값입니다.")
  private String email;
}
