package com.example.deukgeun.trainer.application.dto.request.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = GroupNameValidator.class)
public @interface ValidGroupName {
  String message() default "그룹명을 입력 해주세요.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
