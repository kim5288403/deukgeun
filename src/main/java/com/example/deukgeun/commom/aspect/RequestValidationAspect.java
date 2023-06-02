package com.example.deukgeun.commom.aspect;

import com.example.deukgeun.commom.exception.RequestValidException;
import com.example.deukgeun.commom.service.implement.ValidateServiceImpl;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Map;

@Aspect
@Component
public class RequestValidationAspect {

    @Autowired
    private ValidateServiceImpl validateService;

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

    @After(value = "@annotation(org.springframework.web.bind.annotation.RequestMapping) && args(.., bindingResult)")
    public void handleBindingResult(BindingResult bindingResult) {
        System.out.println("====================handleBindingResult======================");
        if (bindingResult.hasErrors()) {
            Map<String, String> validatorResult = validateService.fieldErrorsMessageHandling(bindingResult);

            validatorResult = validateService.globalErrorsMessageHandling(validatorResult, bindingResult);

            throw new RequestValidException(validatorResult, "request error!");
        }

    }


}
