package com.example.deukgeun.commom.request;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmailRequest {
  @NotBlank(message = "email은 필수 입력 값입니다.")
  private String email;
}
