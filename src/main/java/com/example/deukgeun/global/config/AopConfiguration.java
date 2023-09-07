package com.example.deukgeun.global.config;

import com.example.deukgeun.global.aspect.RequestValidationAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class AopConfiguration {

    /**
     * 이 메서드는 Spring Bean을 생성하여 RequestValidationAspect를 구성합니다.
     *
     * @return RequestValidationAspect 객체를 반환합니다.
     */
    @Bean(name = "requestValidationAspectBean")
    public RequestValidationAspect requestValidationAspect() {
        return new RequestValidationAspect();
    }
}
