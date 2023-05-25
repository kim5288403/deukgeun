package com.example.deukgeun.trainer.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import com.example.deukgeun.commom.service.implement.ValidateServiceImpl;
import com.example.deukgeun.trainer.service.implement.UserServiceImpl;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GroupNameValidator implements ConstraintValidator<ValidGroupName, Object>{
  private final ValidateServiceImpl validateService;
  private final UserServiceImpl userService;
  

  @Override
  public boolean isValid(Object object, ConstraintValidatorContext context) {
    String groupStatus = validateService.getFieldValue(object, "groupStatus");
    if (groupStatus == null) {
      return false;
    }

    String groupName = validateService.getFieldValue(object, "groupName");

    return userService.isEmptyGroupName(groupName, groupStatus);
  }
  
  
}
