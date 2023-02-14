package com.example.deukgeun.commom.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.deukgeun.commom.service.implement.ValidateServiceImpl;

public class PasswordConfirmValidator implements ConstraintValidator<ValidPasswordConfirm, Object> {

  @Autowired
  private ValidateServiceImpl validateService;
  
  @Override
  public void initialize(ValidPasswordConfirm constraintAnnotation) {}

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
