package com.example.deukgeun.global.config;

import com.example.deukgeun.global.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

	@Value("${trainer.role}")
    private String TRAINER_ROLE;
    @Value("${member.role}")
    private String MEMBER_ROLE;
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 이 메서드는 Spring Security를 구성하는 WebSecurityCustomizer Bean을 생성합니다.
     *
     * @return WebSecurityCustomizer 객체를 반환합니다.
     */
    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring().mvcMatchers(
                "/v3/api-docs/**", // Swagger API 문서 엔드포인트를 무시합니다.
                "/swagger-ui/**",  // Swagger UI 페이지를 무시합니다.
                "/api/v1/login" // 로그인 엔드포인트를 무시합니다.
        );
    }

    /**
     * 이 메서드는 Spring Security의 SecurityFilterChain을 설정하고 구성합니다.
     *
     * @param http HttpSecurity 객체를 받아와서 구성합니다.
     * @return SecurityFilterChain 객체를 반환합니다.
     * @throws Exception 예외가 발생할 수 있습니다.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .cors().and() // Cross-Origin Resource Sharing (CORS) 설정을 활성화합니다.
                .csrf().disable() // CSRF(Cross-Site Request Forgery) 보호를 비활성화합니다.
                .authorizeRequests()
                .antMatchers("/api/trainer").hasRole(TRAINER_ROLE)
                .antMatchers("/api/member").hasRole(MEMBER_ROLE)
                .and()
                .addFilterBefore(jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 관리를 STATELESS로 설정합니다.
                .and()
                .headers()
                .frameOptions().sameOrigin() // 같은 출처의 프레임에서만 렌더링을 허용합니다.
                .and().build();
    }

}
