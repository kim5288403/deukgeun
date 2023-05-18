package com.example.deukgeun.commom.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import com.example.deukgeun.commom.repository.AuthMailRepository;
import com.example.deukgeun.commom.service.implement.MailServiceImpl;
import com.example.deukgeun.commom.service.implement.ValidateServiceImpl;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthMailCodeValidator implements ConstraintValidator<ValidAuthMailCode, Object>{
  private final MailServiceImpl mailService;
  private final ValidateServiceImpl validateService;
  
  @Override
  public boolean isValid(Object object, ConstraintValidatorContext context) {
    String email = validateService.getFieldValue(object, "email");
    String code = validateService.getFieldValue(object, "code");

    return mailService.confirmMail(email, code);
  }

}
