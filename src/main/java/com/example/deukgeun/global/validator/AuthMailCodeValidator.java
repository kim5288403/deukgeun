package com.example.deukgeun.global.validator;

import com.example.deukgeun.authMail.application.service.AuthMailApplicationService;
import com.example.deukgeun.global.util.ValidateUtil;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class AuthMailCodeValidator implements ConstraintValidator<ValidAuthMailCode, Object>{
  private final AuthMailApplicationService authMailApplicationService;

  /**
   * 주어진 객체에서 이메일 및 코드 필드 값을 사용하여 유효성을 검사합니다.
   *
   * @param object  검사할 객체입니다.
   * @param context 제약 조건 검사 컨텍스트입니다.
   * @return 이메일과 코드가 일치하는 경우 true, 그렇지 않으면 false를 반환합니다.
   */
  @Override
  public boolean isValid(Object object, ConstraintValidatorContext context) {
    String email = ValidateUtil.getFieldValue(object, "email");
    String code = ValidateUtil.getFieldValue(object, "code");

    return authMailApplicationService.existsByEmailAndCode(email, code);
  }

}
