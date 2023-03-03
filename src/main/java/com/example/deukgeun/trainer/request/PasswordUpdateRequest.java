package com.example.deukgeun.trainer.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PasswordUpdateRequest {
  
  @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
  private String password;
  
  @NotBlank(message = "변경 비밀번호는 필수 입력 값입니다.")
  @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}",
      message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
  private String newPassword;
  
  @NotBlank(message = "변경 비밀번호 확인은 필수 입력 값입니다.")
  private String newPasswordConfirm;
}
