package com.example.deukgeun.trainer.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LoginValidator.class)
public @interface ValidLogin {
  String message() default "비밀번호 혹은 이메일을 확인해주세요.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
