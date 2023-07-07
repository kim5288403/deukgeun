package com.example.deukgeun.global.aspect;

import com.example.deukgeun.global.exception.RequestValidException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Aspect
@Component
public class RequestValidationAspect {


    /**
     * 요청 DTO가 누락된 경우를 확인하여 요청을 유효성 검사합니다.
     * 만약 어떤 요청 DTO가 null인 경우 IllegalArgumentException을 발생시킵니다.
     *
     * @param joinPoint 메서드를 가로채는(joinPoint) 지점을 나타내는 JoinPoint 객체입니다.
     * @throws IllegalArgumentException 만약 어떤 요청 DTO가 null인 경우 발생합니다.
     */
    @Before("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void validateRequest(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg == null) {
                throw new IllegalArgumentException("Request DTO is missing");
            }
        }
    }

    /**
     * BindingResult를 처리하는 메서드입니다.
     *
     * @param bindingResult bindingResult 유효성 검사 결과를 담고 있는 BindingResult 객체
     */
    @After(value = "@annotation(org.springframework.web.bind.annotation.RequestMapping) && args(.., bindingResult)")
    public void handleBindingResultTypeA(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> validatorResult = fieldErrorsMessageHandling(bindingResult);

            validatorResult = globalErrorsMessageHandling(validatorResult, bindingResult);

            throw new RequestValidException(validatorResult, "request error!");
        }
    }

    /**
     * BindingResult를 처리하는 메서드입니다.
     *
     * @param bindingResult bindingResult 유효성 검사 결과를 담고 있는 BindingResult 객체
     */
    @After(value = "@annotation(org.springframework.web.bind.annotation.RequestMapping) && args(*, bindingResult, *)")
    public void handleBindingResultTypeB(BindingResult bindingResult) {
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
    private Map<String, String> fieldErrorsMessageHandling (BindingResult bindingResult) {
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
    private Map<String, String> globalErrorsMessageHandling (
            Map<String, String> validatorResult,
            BindingResult bindingResult) {

        for (ObjectError error : bindingResult.getAllErrors()) {
            String validKeyName = String.format("valid_%s", error.getObjectName());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }

        return validatorResult;
    }


}
