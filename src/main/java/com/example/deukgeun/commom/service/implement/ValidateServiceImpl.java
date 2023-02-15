package com.example.deukgeun.commom.service.implement;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.server.ServerErrorException;
import com.example.deukgeun.commom.exception.RequestValidException;
import com.example.deukgeun.commom.service.ValidateService;

@Service
public class ValidateServiceImpl implements ValidateService{
  
  public void errorMessageHandling(BindingResult bindingResult) {
    Map<String, String> validatorResult = new HashMap<>();

    /* 유효성 검사에 실패한 필드 목록을 받음 */
    for (FieldError error : bindingResult.getFieldErrors()) {
      String validKeyName = String.format("valid_%s", error.getField());
      validatorResult.put(validKeyName, error.getDefaultMessage());
    }

    /* 비밀 번호 확인 애러 목록을 받음 */
    ObjectError objcetError = bindingResult.getGlobalError();
    if (objcetError != null) {
      String errorFildName = objcetError.getCode().replace("Valid", "");
      String validKeyName = String.format("valid_%s", errorFildName);
      validatorResult.put(validKeyName, objcetError.getDefaultMessage());
    }
    
    throw new RequestValidException(validatorResult, "request error!");
  }
  
  @SuppressWarnings("deprecation")
  public String getFieldValue(Object object, String fieldName) {
    Class<?> clazz = object.getClass();
    Field dateField;
    try {
      dateField = clazz.getDeclaredField(fieldName);
      dateField.setAccessible(true);
      Object target = dateField.get(object);
      
      if ((target instanceof Enum)) {
        return target.toString();
      }
      
      if (!(target instanceof String)) {
        return "";
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
