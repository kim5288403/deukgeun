package com.example.deukgeun.trainer.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.deukgeun.commom.enums.Gender;
import com.example.deukgeun.commom.validator.ValidAuthEmail;
import com.example.deukgeun.commom.validator.ValidDupliEmail;
import com.example.deukgeun.commom.validator.ValidEnum;
import com.example.deukgeun.commom.validator.ValidPasswordConfirm;
import com.example.deukgeun.trainer.entity.GroupStatus;
import com.example.deukgeun.trainer.entity.User;
import com.example.deukgeun.trainer.validator.ValidGroupName;
import lombok.Data;

@Data
@ValidPasswordConfirm
@ValidAuthEmail
@ValidGroupName
public class JoinRequest {

  @NotBlank(message = "이름 필수 입력 값입니다.")
  @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z-_]{2,10}$", message = "이름은 한글 2~10자리여야 합니다.")
  private String name;

  @NotBlank(message = "이메일 필수 입력 값입니다.")
  @Email(message = "이메일 형식이 아닙니다.")
  @ValidDupliEmail(message = "이미 존재하는 이메일 입니다.")
  private String email;

  @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
  @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}",
      message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
  private String password;

  @NotBlank(message = "비밀번호 확인은 필수 입력 값입니다.")
  private String passwordConfirm;
  
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
  
  private String code;
  
  @NotBlank(message = "자기소개는 필수 입력 값입니다.")
  private String introduction;
  

  public static User create(JoinRequest request, PasswordEncoder passwordEncoder) {
    return User
        .builder()
        .name(request.getName())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .gender(request.gender)
        .groupStatus(request.getGroupStatus())
        .groupName(request.getGroupName())
        .postcode(request.getPostcode())
        .jibunAddress(request.getJibunAddress())
        .roadAddress(request.getRoadAddress())
        .detailAddress(request.getDetailAddress())
        .extraAddress(request.getExtraAddress())
        .price(request.price)
        .introduction(request.introduction)
        .build();
  }

}
