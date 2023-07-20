package com.example.deukgeun.authMail.application.dto.request;

import com.example.deukgeun.global.validator.ValidAuthMailCode;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@ValidAuthMailCode
public class AuthMailRequest {
  
  @NotBlank(message = "이메일 필수 입력 값입니다.")
  @Email(message = "이메일 형식이 아닙니다.")
  private String email;
  
  @NotBlank(message = "인증번호는 필수 입력 값입니다.")
  @Length(min = 8, max = 8, message = "인증번호는 8글자 입니다.")
  private String code;
}
