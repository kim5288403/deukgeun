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

    /**
     * 이 메서드는 Spring AOP 사용하여 @RequestMapping 어노테이션이 적용된 메서드가 실행되기 전에 호출됩니다.
     *
     * @param joinPoint 메서드를 가로채는(joinPoint) 지점을 나타내는 JoinPoint 객체입니다.
     */
    @Before("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void logBeforeMethod(JoinPoint joinPoint) {
        log.info("Before executing: {}", joinPoint.getSignature());
    }

    /**
     * 이 메서드는 Spring AOP 사용하여 @RequestMapping 어노테이션이 적용된 메서드가 실행된 후에 호출됩니다.
     * 메서드의 반환값과 함께 실행 후에 로그를 출력합니다.
     *
     * @param joinPoint 메서드를 가로채는(joinPoint) 지점을 나타내는 JoinPoint 객체입니다.
     * @param result 메서드의 반환값입니다.
     */
    @AfterReturning(pointcut = "@annotation(org.springframework.web.bind.annotation.RequestMapping)", returning = "result")
    public void logAfterReturningMethod(JoinPoint joinPoint, Object result) {
        log.info("After executing: {}. Result: {}", joinPoint.getSignature(), result);
    }

    /**
     * 이 메서드는 Spring AOP를 사용하여 @RequestMapping 어노테이션이 적용된 메서드가 예외를 던질 때 호출됩니다.
     * 메서드 실행 중에 발생한 예외와 메서드 시그니처를 에러 로그로 출력합니다.
     *
     * @param joinPoint 메서드를 가로채는(joinPoint) 지점을 나타내는 JoinPoint 객체입니다.
     * @param exception 발생한 예외 객체입니다.
     */
    @AfterThrowing(pointcut = "@annotation(org.springframework.web.bind.annotation.RequestMapping)", throwing = "exception")
    public void logAfterThrowingMethod(JoinPoint joinPoint, Exception exception) {
        log.error("Exception thrown in {}: {}", joinPoint.getSignature(), exception.getMessage());
    }
}
