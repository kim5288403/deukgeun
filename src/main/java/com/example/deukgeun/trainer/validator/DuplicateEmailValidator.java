package com.example.deukgeun.trainer.validator;

import com.example.deukgeun.global.validator.ValidDuplicateEmail;
import com.example.deukgeun.trainer.infrastructure.persistence.TrainerServiceImpl;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class DuplicateEmailValidator implements ConstraintValidator<ValidDuplicateEmail, String>{

  private final TrainerServiceImpl trainerService;
  
  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value.isEmpty()) {
      return false;
    }
    return !trainerService.isDuplicateEmail(value);
  }
}
