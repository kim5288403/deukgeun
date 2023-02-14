package com.example.deukgeun.commom.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = EmailAuthValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidAuthEmail {
  String message() default "이메일 인증을 해주세요.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
