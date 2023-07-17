package com.example.deukgeun.global.validator;

import com.example.deukgeun.auth.application.service.implement.AuthMailApplicationServiceImpl;
import com.example.deukgeun.global.util.ValidateUtil;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class AuthMailCodeValidator implements ConstraintValidator<ValidAuthMailCode, Object>{
  private final AuthMailApplicationServiceImpl authMailApplicationService;

  @Override
  public boolean isValid(Object object, ConstraintValidatorContext context) {
    String email = ValidateUtil.getFieldValue(object, "email");
    String code = ValidateUtil.getFieldValue(object, "code");

    return authMailApplicationService.existsByEmailAndCode(email, code);
  }

}
