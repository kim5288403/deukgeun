package com.example.deukgeun.trainer.validator;

import com.example.deukgeun.global.util.ValidateUtil;
import com.example.deukgeun.trainer.infrastructure.persistence.TrainerServiceImpl;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class GroupNameValidator implements ConstraintValidator<ValidGroupName, Object>{
  private final TrainerServiceImpl trainerService;
  

  @Override
  public boolean isValid(Object object, ConstraintValidatorContext context) {
    String groupStatus = ValidateUtil.getFieldValue(object, "groupStatus");
    if (groupStatus == null) {
      return false;
    }
    String groupName = ValidateUtil.getFieldValue(object, "groupName");

    return trainerService.isEmptyGroupName(groupName, groupStatus);
  }
  
  
}
