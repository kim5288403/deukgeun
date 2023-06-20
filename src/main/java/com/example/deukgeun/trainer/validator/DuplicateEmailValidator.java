package com.example.deukgeun.trainer.validator;

import com.example.deukgeun.commom.validator.ValidDuplicateEmail;
import com.example.deukgeun.trainer.service.implement.MemberServiceImpl;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class DuplicateEmailValidator implements ConstraintValidator<ValidDuplicateEmail, String>{

  private final MemberServiceImpl memberService;
  
  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value.isEmpty()) {
      return false;
    }
    return ! memberService.isDuplicateEmail(value);
  }
}
