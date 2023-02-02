package com.example.deukgeun.commom.validator;

import java.lang.reflect.Field;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.web.server.ServerErrorException;

public class PasswordConfirmValidator implements ConstraintValidator<ValidPasswordConfirm, Object> {
  @Override
  public void initialize(ValidPasswordConfirm constraintAnnotation) {}

  @Override
  public boolean isValid(Object object, ConstraintValidatorContext context) {
    boolean flag = true;

    String password = getFieldValue(object, "password");
    String confirm = getFieldValue(object, "passwordConfirm");

    if (!password.equals(confirm)) {
      flag = false;
    }

    return flag;
  }

  @SuppressWarnings("deprecation")
  private String getFieldValue(Object object, String fieldName) {
    Class<?> clazz = object.getClass();
    Field dateField;
    try {

      dateField = clazz.getDeclaredField(fieldName);
      dateField.setAccessible(true);
      Object target = dateField.get(object);

      if (!(target instanceof String)) {
        throw new ClassCastException("casting exception");
      }

      return (String) target;
    } catch (NoSuchFieldException e) {
      System.out.println("NoSuchFieldException : " + e.getMessage());
    } catch (IllegalAccessException e) {
      System.out.println("IllegalAccessException : " + e.getMessage());
    }
    throw new ServerErrorException("Not Found Field");
  }

}
