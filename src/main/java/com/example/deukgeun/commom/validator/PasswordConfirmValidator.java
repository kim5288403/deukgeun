package com.example.deukgeun.commom.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import com.example.deukgeun.commom.service.implement.ValidateServiceImpl;
import com.example.deukgeun.trainer.service.implement.UserServiceImpl;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PasswordConfirmValidator implements ConstraintValidator<ValidPasswordConfirm, Object> {

  private final ValidateServiceImpl validateService;
  private final UserServiceImpl  userService;

  @Override
  public boolean isValid(Object object, ConstraintValidatorContext context) {

    String password = validateService.getFieldValue(object, "password");
    String confirm = validateService.getFieldValue(object, "passwordConfirm");

    return userService.isPasswordConfirmation(password, confirm);
  }


}
