package com.example.deukgeun.trainer.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SaveLicenseRequest {
  
  @NotBlank(message = "이름 필수 입력 값입니다.")
  private String name;
  
  @NotBlank(message = "자격증 번호는 필수 입력 값입니다.")
  private String no;
}
