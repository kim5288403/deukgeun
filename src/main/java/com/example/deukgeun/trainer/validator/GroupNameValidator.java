package com.example.deukgeun.trainer.validator;

import com.example.deukgeun.commom.service.implement.ValidateServiceImpl;
import com.example.deukgeun.trainer.service.implement.TrainerServiceImpl;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class GroupNameValidator implements ConstraintValidator<ValidGroupName, Object>{
  private final ValidateServiceImpl validateService;
  private final TrainerServiceImpl trainerService;
  

  @Override
  public boolean isValid(Object object, ConstraintValidatorContext context) {
    String groupStatus = validateService.getFieldValue(object, "groupStatus");
    if (groupStatus == null) {
      return false;
    }

    String groupName = validateService.getFieldValue(object, "groupName");

    return trainerService.isEmptyGroupName(groupName, groupStatus);
  }
  
  
}
