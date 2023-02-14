package com.example.deukgeun.commom.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.deukgeun.trainer.repository.UserRepository;

public class EmailDupliValidator implements ConstraintValidator<ValidDupliEmail, String>{
  @Autowired
  private UserRepository userRepository;
  
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
