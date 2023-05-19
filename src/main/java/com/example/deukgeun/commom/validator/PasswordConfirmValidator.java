package com.example.deukgeun.commom.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import com.example.deukgeun.commom.service.implement.ValidateServiceImpl;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PasswordConfirmValidator implements ConstraintValidator<ValidPasswordConfirm, Object> {

  private final ValidateServiceImpl validateService;

  @Override
  public boolean isValid(Object object, ConstraintValidatorContext context) {
    boolean flag = true;

    String password = validateService.getFieldValue(object, "password");
    String confirm = validateService.getFieldValue(object, "passwordConfirm");

    if (!password.equals(confirm)) {
      flag = false;
    }

    return flag;
  }


}
