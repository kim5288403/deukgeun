package com.example.deukgeun.commom.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import com.example.deukgeun.trainer.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EmailDupliValidator implements ConstraintValidator<ValidDuplicateEmail, String>{

  private final UserRepository userRepository;
  
  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    
    boolean emailDuplicate = false;
    emailDuplicate = userRepository.existsByEmail(value);
    
    if (emailDuplicate) {
      return false;
    }
    
    return true;
  }
}
