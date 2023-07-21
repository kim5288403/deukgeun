package com.example.deukgeun.trainer.application.dto.request.validator;

import com.example.deukgeun.global.validator.ValidDuplicateEmail;
import com.example.deukgeun.trainer.application.service.TrainerApplicationService;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class DuplicateEmailValidator implements ConstraintValidator<ValidDuplicateEmail, String>{

  private final TrainerApplicationService trainerService;
  
  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value.isEmpty()) {
      return false;
    }
    return !trainerService.existsByEmail(value);
  }
}
