package com.example.deukgeun.trainer.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import com.example.deukgeun.commom.service.implement.ValidateServiceImpl;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GroupNameValidator implements ConstraintValidator<ValidGroupName, Object>{
  private final ValidateServiceImpl validateService;
  

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
