package com.example.deukgeun.auth.application.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmailRequest {
  @NotBlank(message = "email 은 필수 입력 값입니다.")
  private String email;
}
