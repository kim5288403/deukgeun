package com.example.deukgeun.commom.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = EmailDupliValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDupliEmail {
  String message() default "중복된 이메일 입니다.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
