package com.example.deukgeun.trainer.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import com.example.deukgeun.commom.validator.ValidPasswordUpdate;
import com.example.deukgeun.trainer.validator.ValidEmailAndPw;
import lombok.Data;

@Data
@ValidEmailAndPw
@ValidPasswordUpdate
public class UpdatePasswordRequest {
  
  @NotBlank(message = "이메일 필수 입력 값입니다.")
  @Email(message = "이메일 형식이 아닙니다.")
  private String email;
  
  @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
  private String password;
  
  @NotBlank(message = "변경 비밀번호는 필수 입력 값입니다.")
  @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}",
      message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
  private String newPassword;
  
  @NotBlank(message = "변경 비밀번호 확인은 필수 입력 값입니다.")
  private String newPasswordConfirm;
}
