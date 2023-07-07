package com.example.deukgeun.global.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = AccessEmailValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidAccessEmail {
  String message() default "이메일 인증을 해주세요.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
