package com.example.deukgeun.member.request;

import com.example.deukgeun.global.enums.Gender;
import com.example.deukgeun.global.validator.ValidAccessEmail;
import com.example.deukgeun.global.validator.ValidEnum;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class JoinRequest {

    @NotBlank(message = "이름 필수 입력 값입니다.")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z-_]{2,10}$", message = "이름은 한글 2~10자리여야 합니다.")
    private String name;

    @NotNull(message = "나이는 필수 입력 값입니다.")
    private Integer age;

    @NotBlank(message = "이메일 필수 입력 값입니다.")
    @Email(message = "이메일 형식이 아닙니다.")
    @ValidAccessEmail
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}",
            message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String password;

    @NotBlank(message = "비밀번호 확인은 필수 입력 값입니다.")
    private String passwordConfirm;

    @ValidEnum(enumClass = Gender.class, message = "잘못된 성별 값입니다.")
    private Gender gender;
}
