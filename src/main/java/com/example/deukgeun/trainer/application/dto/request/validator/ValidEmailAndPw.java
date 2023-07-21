package com.example.deukgeun.trainer.application.dto.request.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.TYPE)
@Constraint(validatedBy = EmailAndPwValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEmailAndPw {
  String message() default "기존 비밀번호가 일치하지 않습니다.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
