package com.example.deukgeun.commom.service.implement;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import com.example.deukgeun.commom.exception.RequestValidException;
import com.example.deukgeun.commom.service.ValidateService;

@Service
public class ValidateServiceImpl implements ValidateService{
  public Map<String, String> errorMessageHandling(BindingResult bindingResult) {
    Map<String, String> validatorResult = new HashMap<>();

    /* 유효성 검사에 실패한 필드 목록을 받음 */
    for (FieldError error : bindingResult.getFieldErrors()) {
      String validKeyName = String.format("valid_%s", error.getField());
      validatorResult.put(validKeyName, error.getDefaultMessage());
    }

    /* 비밀 번호 확인 애러 목록을 받음 */
    ObjectError objcetError = bindingResult.getGlobalError();
    if (objcetError != null) {
      String validKeyName = String.format("valid_%s", "passwordConfirm");
      validatorResult.put(validKeyName, objcetError.getDefaultMessage());
    }
    
    return validatorResult;
  }
  
  public void errorMessageHandling2(BindingResult bindingResult) {
    Map<String, String> validatorResult = new HashMap<>();

    /* 유효성 검사에 실패한 필드 목록을 받음 */
    for (FieldError error : bindingResult.getFieldErrors()) {
      String validKeyName = String.format("valid_%s", error.getField());
      validatorResult.put(validKeyName, error.getDefaultMessage());
    }

    /* 비밀 번호 확인 애러 목록을 받음 */
    ObjectError objcetError = bindingResult.getGlobalError();
    if (objcetError != null) {
      String validKeyName = String.format("valid_%s", "passwordConfirm");
      validatorResult.put(validKeyName, objcetError.getDefaultMessage());
    }
    
    throw new RequestValidException(validatorResult, "request error!");
    
  }
}
