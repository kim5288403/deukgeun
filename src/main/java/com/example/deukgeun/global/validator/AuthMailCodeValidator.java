package com.example.deukgeun.global.validator;

import com.example.deukgeun.main.service.implement.AuthMailServiceImpl;
import com.example.deukgeun.main.service.implement.ValidateServiceImpl;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class AuthMailCodeValidator implements ConstraintValidator<ValidAuthMailCode, Object>{
  private final AuthMailServiceImpl mailService;
  private final ValidateServiceImpl validateService;
  
  @Override
  public boolean isValid(Object object, ConstraintValidatorContext context) {
    String email = validateService.getFieldValue(object, "email");
    String code = validateService.getFieldValue(object, "code");

    return mailService.confirmMail(email, code);
  }

}
