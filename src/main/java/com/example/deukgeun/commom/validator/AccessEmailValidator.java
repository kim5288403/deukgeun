package com.example.deukgeun.commom.validator;

import com.example.deukgeun.commom.service.implement.AuthMailServiceImpl;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class AccessEmailValidator implements ConstraintValidator<ValidAccessEmail, String>{

  private final AuthMailServiceImpl mailService;

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value.isEmpty()) {
      return false;
    }
    try {
      return mailService.isEmailAuthenticated(value);
    } catch (Exception e) {
      return false;
    }
  }
}
