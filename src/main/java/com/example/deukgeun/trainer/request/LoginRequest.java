package com.example.deukgeun.trainer.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import com.example.deukgeun.trainer.validator.ValidLogin;
import lombok.Data;

@Data
@ValidLogin
public class LoginRequest {
  
  @NotBlank(message = "이메일 필수 입력 값입니다.")
  @Email(message = "이메일 형식이 아닙니다.")
  private String email;
  
  @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
  private String password;
  
}
