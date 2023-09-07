package com.example.deukgeun.global.validator;

import com.example.deukgeun.global.util.ValidateUtil;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class PasswordUpdateValidator implements ConstraintValidator<ValidPasswordUpdate, Object>{

  /**
   * 주어진 객체에서 새 비밀번호와 새 비밀번호 확인 필드 값의 일치 여부를 검사합니다.
   *
   * @param object  검사할 객체입니다.
   * @param context 제약 조건 검사 컨텍스트입니다.
   * @return 새 비밀번호와 새 비밀번호 확인 필드 값이 일치하는 경우 true, 그렇지 않으면 false를 반환합니다.
   */
  @Override
  public boolean isValid(Object object, ConstraintValidatorContext context) {
    boolean flag = false;

    String newPassword = ValidateUtil.getFieldValue(object, "newPassword");
    String newPasswordConfirm = ValidateUtil.getFieldValue(object, "newPasswordConfirm");

    assert newPassword != null;
    if (newPassword.equals(newPasswordConfirm)) {
      flag = true;
    }
    
    return flag;
  }

}
