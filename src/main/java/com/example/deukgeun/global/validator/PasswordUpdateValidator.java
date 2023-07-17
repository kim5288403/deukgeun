package com.example.deukgeun.global.validator;

import com.example.deukgeun.global.util.ValidateUtil;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class PasswordUpdateValidator implements ConstraintValidator<ValidPasswordUpdate, Object>{
  

  @Override
  public boolean isValid(Object object, ConstraintValidatorContext context) {
    boolean flag = false;

    String newPassword = ValidateUtil.getFieldValue(object, "newPassword");
    String newPasswordConfirm = ValidateUtil.getFieldValue(object, "newPasswordConfirm");
     
    if (newPassword.equals(newPasswordConfirm)) {
      flag = true;
    }
    
    return flag;
  }

}
