package com.example.deukgeun.commom.service.implement;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import com.example.deukgeun.commom.exception.RequestValidException;
import com.example.deukgeun.commom.service.ValidateService;

@Service
public class ValidateServiceImpl implements ValidateService{

  /**
   * 1. 유효성 필드 애러가 있을경우 애러를 map 타입으로 매핑
   * 2. 유효성 글로벌 애러가 있을경우  애러를 map 타입으로 매핑
   * 3. RequestValidException 발생시킴
   *
   * @param bindingResult request validator 에서 error 내용을 담고있음
   */
  public void requestValidExceptionHandling(BindingResult bindingResult) {

    if (bindingResult.hasErrors()) {
      /* 유효성 검사에 실패한 필드 애러 목록을 받음 */
      Map<String, String> validatorResult = fieldErrorsMessageHandling(bindingResult);

      /* 유효성 검사에 실패한 글로벌 애러 목록을 받음 */
      validatorResult = globalErrorsMessageHandling(validatorResult, bindingResult);

      throw new RequestValidException(validatorResult, "request error!");
    }
  }

  /**
   * 유효성 필드 애러가 있을경우 애러를 map 타입으로 매핑
   *
   * @param bindingResult request validator 에서 error 내용을 담고있음
   * @return bindingResult 에 필드 애러내용 to Map<>
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
   * 유효성 글로벌 애러가 있을경우  애러를 map 타입으로 매핑
   *
   * @param validatorResult 필드에러 매핑을 걸친 map
   * @param bindingResult request validator 에서 error 내용을 담고있음
   * @return bindingResult 에 글로벌 애러내용 to Map<>
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
   * Object 에서 field 값 추출
   *
   * @param object error 를 담고있는 object
   * @param fieldName 추출하고자 하는 field
   * @return target 추출한 field
   */
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
