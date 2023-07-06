package com.example.deukgeun.global.config;

import com.example.deukgeun.global.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
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
public class SecurityConfig{
  
  	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public WebSecurityCustomizer configure() {
	  return (web) -> web.ignoring().mvcMatchers(
	      "/v3/api-docs/**",
	      "/swagger-ui/**",
	      "/api/v1/login"
	  );
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http
		.cors().and()
		.csrf().disable()
		.authorizeRequests()
		.antMatchers("/trainer").permitAll()
		.antMatchers("/member").permitAll()
		.and()
		.addFilterBefore(jwtAuthenticationFilter,
            UsernamePasswordAuthenticationFilter.class)
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		.headers()
        .frameOptions().sameOrigin().and().build();
	}

}
