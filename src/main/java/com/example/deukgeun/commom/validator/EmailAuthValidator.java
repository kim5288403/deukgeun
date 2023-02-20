package com.example.deukgeun.commom.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import com.example.deukgeun.commom.enums.MailStatus;
import com.example.deukgeun.commom.repository.AuthMailRepository;
import com.example.deukgeun.commom.service.implement.ValidateServiceImpl;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EmailAuthValidator implements ConstraintValidator<ValidAuthEmail, Object>{
  
  private final AuthMailRepository authMailRepository;
  private final ValidateServiceImpl validateService;

  @Override
  public boolean isValid(Object object, ConstraintValidatorContext context) {
    String email = validateService.getFieldValue(object, "email");
    String code = validateService.getFieldValue(object, "code");
    
    boolean check = authMailRepository.existsByEmailAndCodeAndStatus(email, code, MailStatus.Y);
    
    if (check) {
      return true;
    }
    
    return false;
  }
  

}
