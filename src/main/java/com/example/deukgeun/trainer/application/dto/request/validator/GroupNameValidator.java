package com.example.deukgeun.trainer.application.dto.request.validator;

import com.example.deukgeun.global.util.ValidateUtil;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class GroupNameValidator implements ConstraintValidator<ValidGroupName, Object>{

  @Override
  public boolean isValid(Object object, ConstraintValidatorContext context) {
    String groupStatus = ValidateUtil.getFieldValue(object, "groupStatus");
    if (groupStatus == null) {
      return false;
    }
    String groupName = ValidateUtil.getFieldValue(object, "groupName");

    return !groupStatus.equals("Y") || !groupName.isEmpty();
  }
  
  
}
