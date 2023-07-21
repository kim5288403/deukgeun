package com.example.deukgeun.global.validator;

import com.example.deukgeun.trainer.application.dto.request.validator.DuplicateEmailValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = DuplicateEmailValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDuplicateEmail {
  String message() default "중복된 이메일 입니다.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
