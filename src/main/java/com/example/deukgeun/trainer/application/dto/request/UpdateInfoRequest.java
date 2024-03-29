package com.example.deukgeun.trainer.application.dto.request;

import com.example.deukgeun.global.enums.Gender;
import com.example.deukgeun.global.validator.ValidEnum;
import com.example.deukgeun.trainer.domain.model.valueobjcet.GroupStatus;
import com.example.deukgeun.trainer.application.dto.request.validator.ValidGroupName;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@ValidGroupName
public class UpdateInfoRequest {
  @NotBlank(message = "이메일 필수 입력 값입니다.")
  @Email(message = "이메일 형식이 아닙니다.")
  private String email;

  @NotBlank(message = "이름 필수 입력 값입니다.")
  @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z-_]{2,10}$", message = "이름은 한글 2~10자리여야 합니다.")
  private String name;
  
  @NotBlank(message = "우편번호는 필수 입력 값입니다.")
  private String postcode;
  
  private String jibunAddress;

  private String roadAddress;

  private String detailAddress;

  private String extraAddress;
  
  @NotNull(message = "PT 가격은 필수 입력 값입니다.")
  private Integer price;

  @ValidEnum(enumClass = Gender.class, message = "잘못된 성별 값입니다.")
  private Gender gender;

  @ValidEnum(enumClass = GroupStatus.class, message = "잘못된 소속 값입니다.")
  private GroupStatus groupStatus;

  private String groupName;
  
  @NotBlank(message = "자기소개는 필수 입력 값입니다.")
  private String introduction;

}
