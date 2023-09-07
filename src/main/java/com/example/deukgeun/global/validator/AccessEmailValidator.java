package com.example.deukgeun.global.validator;

import com.example.deukgeun.authMail.application.service.AuthMailApplicationService;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class AccessEmailValidator implements ConstraintValidator<ValidAccessEmail, String>{

  private final AuthMailApplicationService authMailApplicationService;

  /**
   * 주어진 이메일 값이 유효한지 검사합니다.
   *
   * @param value    검사할 이메일 값입니다.
   * @param context  제약 조건 검사 컨텍스트입니다.
   * @return 이메일 값이 유효하면 true, 그렇지 않으면 false를 반환합니다.
   */
  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value.isEmpty()) {
      return false;
    }
    try {
      return authMailApplicationService.isEmailAuthenticated(value);
    } catch (Exception e) {
      return false;
    }
  }
}
