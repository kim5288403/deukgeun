package com.example.deukgeun.global.validator;

import com.example.deukgeun.authMail.application.service.AuthMailApplicationService;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class AccessEmailValidator implements ConstraintValidator<ValidAccessEmail, String>{

  private final AuthMailApplicationService authMailApplicationService;

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value.isEmpty()) {
      return false;
    }
    try {
      return authMailApplicationService.isEmailAuthenticated(value);
    } catch (Exception e) {
      return false;
    }
  }
}
