package com.example.deukgeun.commom.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.deukgeun.commom.enums.MailStatus;
import com.example.deukgeun.commom.repository.AuthMailRepository;
import com.example.deukgeun.commom.service.implement.ValidateServiceImpl;

public class EmailAuthValidator implements ConstraintValidator<ValidAuthEmail, Object>{
  
  @Autowired
  private AuthMailRepository authMailRepository;
  
  @Autowired
  private ValidateServiceImpl validateService;

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
