package com.example.deukgeun.trainer.request;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SaveLicenseRequest {
  
  @NotBlank(message = "이름 필수 입력 값입니다.")
  private String name;
  @NotBlank(message = "자격증 번호는 필수 입력 값입니다.")
  private String no;
  
}
