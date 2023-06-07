package com.example.deukgeun.commom.validator;

import com.example.deukgeun.commom.service.implement.ValidateServiceImpl;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class PasswordConfirmValidator implements ConstraintValidator<ValidPasswordConfirm, Object> {

  private final ValidateServiceImpl validateService;

  @Override
  public boolean isValid(Object object, ConstraintValidatorContext context) {

    String password = validateService.getFieldValue(object, "password");
    String confirm = validateService.getFieldValue(object, "passwordConfirm");

    return password.equals(confirm);
  }


}
