package com.example.deukgeun.commom.service.implement;

import com.example.deukgeun.commom.exception.RequestValidException;
import com.example.deukgeun.commom.service.ValidateService;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import javax.validation.ValidationException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class ValidateServiceImpl implements ValidateService{

  /**
   * 요청 유효성 검사 결과에 대한 예외 처리를 수행합니다.
   *
   * @param bindingResult 요청 유효성 검사 결과
   * @throws RequestValidException 요청 유효성 검사 예외
   */
  public void requestValidExceptionHandling(BindingResult bindingResult) {

    if (bindingResult.hasErrors()) {
      Map<String, String> validatorResult = fieldErrorsMessageHandling(bindingResult);

      validatorResult = globalErrorsMessageHandling(validatorResult, bindingResult);

      throw new RequestValidException(validatorResult, "request error!");
    }
  }

  /**
   * 필드 오류 메시지 처리를 수행합니다.
   *
   * @param bindingResult 요청 유효성 검사 결과
   * @return 필드 오류 메시지 맵
   */
  public Map<String, String> fieldErrorsMessageHandling (BindingResult bindingResult) {
    Map<String, String> validatorResult =  new HashMap<>();

    for (FieldError error : bindingResult.getFieldErrors()) {
      String validKeyName = String.format("valid_%s", error.getField());
      if (error.getField().equals("profile") && Objects.equals(error.getCode(), "typeMismatch")) {
        validatorResult.put(validKeyName, "프로필 이미지 파일은 필수 값입니다.");
      } else {
        validatorResult.put(validKeyName, error.getDefaultMessage());
      }
    }

    return validatorResult;
  }

  /**
   * 전역 오류 메시지 처리를 수행합니다.
   *
   * @param validatorResult 이전에 처리된 필드 오류 메시지 맵
   * @param bindingResult 요청 유효성 검사 결과
   * @return 전역 오류 메시지가 포함된 메시지 맵
   */
  public Map<String, String> globalErrorsMessageHandling (
          Map<String, String> validatorResult,
          BindingResult bindingResult) {

    for (ObjectError error : bindingResult.getAllErrors()) {
      String validKeyName = String.format("valid_%s", error.getObjectName());
      validatorResult.put(validKeyName, error.getDefaultMessage());
    }

    return validatorResult;
  }

  /**
   * 객체의 필드 값을 가져옵니다.
   *
   * @param object    값을 가져올 객체
   * @param fieldName 필드 이름
   * @return 필드의 값 (문자열 형식)
   */
  public String getFieldValue(Object object, String fieldName) {
    Class<?> clazz = object.getClass();
    Field dateField;
    try {
      dateField = clazz.getDeclaredField(fieldName);
      dateField.setAccessible(true);
      Object target = dateField.get(object);

      if (target == null) {
         return null;
      }

      if ((target instanceof Enum)) {
        return target.toString();
      }

      if (!(target instanceof String)) {
        return null;
      }
      
      return target.toString();
    } catch (NoSuchFieldException e) {
      System.out.println("NoSuchFieldException : " + e.getMessage());
    } catch (IllegalAccessException e) {
      System.out.println("IllegalAccessException : " + e.getMessage());
    }

    return null;
  }
}
