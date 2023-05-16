package com.example.deukgeun.commom.service.implement;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import com.example.deukgeun.commom.exception.RequestValidException;
import com.example.deukgeun.commom.service.ValidateService;

@Service
public class ValidateServiceImpl implements ValidateService{

  /**
   * 1. error 내용이 있을경우 애러내용을 map 타입으로 매핑
   * 2. RequestValidException 발생함
   *
   * @param bindingResult request validator 에서 error 내용을 담고있음
   */
  public void errorMessageHandling(BindingResult bindingResult) {

    if (bindingResult.hasErrors()) {
      Map<String, String> validatorResult = new HashMap<>();

      /* 유효성 검사에 실패한 필드 목록을 받음 */
      for (FieldError error : bindingResult.getFieldErrors()) {
        String validKeyName = String.format("valid_%s", error.getField());
        if (error.getField().equals("profile") && Objects.equals(error.getCode(), "typeMismatch")) {
          validatorResult.put(validKeyName, "프로필 이미지 파일은 필수 값입니다.");
        } else {
          validatorResult.put(validKeyName, error.getDefaultMessage());
        }
      }

      /* 비밀 번호 확인 애러 목록을 받음 */
      ObjectError objcetError = bindingResult.getGlobalError();
      if (objcetError != null) {
        String errorFildName = Objects.requireNonNull(objcetError.getCode()).replace("Valid", "");
        String validKeyName = String.format("valid_%s", errorFildName);
        validatorResult.put(validKeyName, objcetError.getDefaultMessage());
      }

      throw new RequestValidException(validatorResult, "request error!");
    }

  }

  /**
   * Object 에서 field 값 추출
   *
   * @param object
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
        return "";
      }
      
      return target.toString();
    } catch (NoSuchFieldException e) {
      System.out.println("NoSuchFieldException : " + e.getMessage());
    } catch (IllegalAccessException e) {
      System.out.println("IllegalAccessException : " + e.getMessage());
    }

    return "";
  }
}
