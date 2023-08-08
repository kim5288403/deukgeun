package com.example.deukgeun.global.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
@Aspect
@Component
@Slf4j
public class LoggingAspect {
    @Before("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void logBeforeMethod(JoinPoint joinPoint) {
        log.info("Before executing: {}", joinPoint.getSignature());
    }

    @AfterReturning(pointcut = "@annotation(org.springframework.web.bind.annotation.RequestMapping)", returning = "result")
    public void logAfterReturningMethod(JoinPoint joinPoint, Object result) {
        log.info("After executing: {}. Result: {}", joinPoint.getSignature(), result);
    }

    @AfterThrowing(pointcut = "@annotation(org.springframework.web.bind.annotation.RequestMapping)", throwing = "exception")
    public void logAfterThrowingMethod(JoinPoint joinPoint, Exception exception) {
        log.error("Exception thrown in {}: {}", joinPoint.getSignature(), exception.getMessage());
    }
}
