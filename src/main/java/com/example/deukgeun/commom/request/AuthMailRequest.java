package com.example.deukgeun.commom.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import com.example.deukgeun.commom.entity.AuthMail;
import lombok.Data;

@Data
public class AuthMailRequest {
  
  @NotBlank(message = "이메일 필수 입력 값입니다.")
  @Email(message = "이메일 형식이 아닙니다.")
  private String email;
  
  @NotBlank(message = "인증번호는 필수 입력 값입니다.")
  @Length(min = 8, max = 8, message = "인증번호는 8글자 입니다.")
  private String code;

  
  public static AuthMail create(String email, String code) {
    return AuthMail
        .builder()
        .email(email)
        .code(code)
        .build();
  }

}
