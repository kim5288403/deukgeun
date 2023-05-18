package com.example.deukgeun.commom.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import com.example.deukgeun.trainer.repository.UserRepository;
import com.example.deukgeun.trainer.service.implement.UserServiceImpl;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DuplicateEmailValidator implements ConstraintValidator<ValidDuplicateEmail, String>{

  private final UserServiceImpl userService;
  
  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    
    return ! userService.isDuplicateEmail(value);
  }
}
