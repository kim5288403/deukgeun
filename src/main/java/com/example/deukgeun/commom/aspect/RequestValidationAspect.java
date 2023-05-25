package com.example.deukgeun.commom.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RequestValidationAspect {

    @Before("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void validateRequest(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg == null) {
                throw new IllegalArgumentException("Request DTO is missing");
            }
        }
    }
}
