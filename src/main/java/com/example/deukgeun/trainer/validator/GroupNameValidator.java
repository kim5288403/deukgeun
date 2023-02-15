package com.example.deukgeun.trainer.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.deukgeun.commom.service.implement.ValidateServiceImpl;

public class GroupNameValidator implements ConstraintValidator<ValidGroupName, Object>{
  @Autowired
  private ValidateServiceImpl validateService;
  

  @Override
  public boolean isValid(Object object, ConstraintValidatorContext context) {

    String groupStatus = validateService.getFieldValue(object, "groupStatus");
    String groupName = validateService.getFieldValue(object, "groupName");
    
    if (groupStatus.equals("Y") && groupName.isEmpty()) {
      return false;
    }
    
    return true;
  }
  
  
}
