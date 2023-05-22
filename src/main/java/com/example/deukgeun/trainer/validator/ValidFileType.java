package com.example.deukgeun.trainer.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Constraint(validatedBy = FileTypeValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidFileType {
    String message() default "잘못된 파일 형식 입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
