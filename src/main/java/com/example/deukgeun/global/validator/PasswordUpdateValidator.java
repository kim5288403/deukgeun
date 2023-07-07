package com.example.deukgeun.global.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import com.example.deukgeun.main.service.implement.ValidateServiceImpl;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PasswordUpdateValidator implements ConstraintValidator<ValidPasswordUpdate, Object>{
  
  private final ValidateServiceImpl validateService;

  
  @Override
  public boolean isValid(Object object, ConstraintValidatorContext context) {
    boolean flag = false;

    String newPassword = validateService.getFieldValue(object, "newPassword");
    String newPasswordConfirm = validateService.getFieldValue(object, "newPasswordConfirm");
     
    if (newPassword.equals(newPasswordConfirm)) {
      flag = true;
    }
    
    return flag;
  }

}
