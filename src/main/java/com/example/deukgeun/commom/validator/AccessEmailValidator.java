package com.example.deukgeun.commom.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import com.example.deukgeun.commom.enums.MailStatus;
import com.example.deukgeun.commom.service.implement.MailServiceImpl;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AccessEmailValidator implements ConstraintValidator<ValidAccessEmail, String>{

  private final MailServiceImpl mailService;

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
