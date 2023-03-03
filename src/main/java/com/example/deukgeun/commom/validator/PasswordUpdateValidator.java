package com.example.deukgeun.commom.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import com.example.deukgeun.commom.service.implement.ValidateServiceImpl;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PasswordUpdateValidator implements ConstraintValidator<ValidPasswordUpdate, Object>{
  
  private final ValidateServiceImpl validateService;
  
  @Override
  public boolean isValid(Object object, ConstraintValidatorContext context) {
    boolean flag = true;

    String password = validateService.getFieldValue(object, "password");

    String newPassword = validateService.getFieldValue(object, "newPassword");
    String newPpasswordConfirm = validateService.getFieldValue(object, "newPpasswordConfirm");

    if (!newPassword.equals(newPpasswordConfirm)) {
      flag = false;
    }

    return flag;
  }

}
