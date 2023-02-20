package com.example.deukgeun.commom.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import com.example.deukgeun.commom.repository.AuthMailRepository;
import com.example.deukgeun.commom.service.implement.ValidateServiceImpl;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthMailCodeValidator implements ConstraintValidator<ValidAuthMailCode, Object>{
  private final AuthMailRepository authMailRepository;
  private final ValidateServiceImpl validateService;
  
  @Override
  public boolean isValid(Object object, ConstraintValidatorContext context) {
    String email = validateService.getFieldValue(object, "email");
    String code = validateService.getFieldValue(object, "code");
    
    boolean check = authMailRepository.existsByEmailAndCode(email, code);
    
    if (check) {
      return true;
    }
    
    return false;
  }

}
