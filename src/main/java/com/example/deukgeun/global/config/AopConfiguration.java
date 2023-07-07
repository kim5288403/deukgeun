package com.example.deukgeun.global.config;

import com.example.deukgeun.global.aspect.RequestValidationAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class AopConfiguration {

    @Bean(name = "requestValidationAspectBean")
    public RequestValidationAspect requestValidationAspect() {
        return new RequestValidationAspect();
    }
}
