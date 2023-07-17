package com.example.deukgeun.global.validator;

import com.example.deukgeun.global.util.ValidateUtil;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class PasswordConfirmValidator implements ConstraintValidator<ValidPasswordConfirm, Object> {


  @Override
  public boolean isValid(Object object, ConstraintValidatorContext context) {

    String password = ValidateUtil.getFieldValue(object, "password");
    String confirm = ValidateUtil.getFieldValue(object, "passwordConfirm");

    return password.equals(confirm);
  }


}
