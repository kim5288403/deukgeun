package com.example.deukgeun.commom.exception.advice;

import java.io.UnsupportedEncodingException;
import javax.mail.MessagingException;
import javax.validation.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.example.deukgeun.commom.exception.RequestValidException;
import com.example.deukgeun.commom.util.RestResponseUtil;
import io.jsonwebtoken.ExpiredJwtException;

@RestControllerAdvice
public class RestControllerAdvisor {
  
  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> exceptionHandler(Exception e) {
    return RestResponseUtil
        .BadResponse(e.getMessage(), null);
  }
  
  @ExceptionHandler(RequestValidException.class)
  public ResponseEntity<?> requestValidException(RequestValidException e) {
    
    return RestResponseUtil
        .BadResponse(e.getMessage(), e.getResult());
  }
  
  @ExceptionHandler(UnsupportedEncodingException.class)
  public ResponseEntity<?> unsupportedEncodingException(UnsupportedEncodingException e) {
    return RestResponseUtil
        .BadResponse(e.getMessage(), null);
  }
  
  @ExceptionHandler(MessagingException.class)
  public ResponseEntity<?> messagingException(MessagingException e) {
    return RestResponseUtil
        .BadResponse(e.getMessage(), null);
  }
  
  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<?> validationException(ValidationException e) {
    return RestResponseUtil
        .BadResponse(e.getMessage(), null);
  }
  
  @ExceptionHandler(ExpiredJwtException.class)
  public ResponseEntity<?> expiredJwtException(ExpiredJwtException e) {
    return RestResponseUtil
        .BadResponse(e.getMessage(), null);
  }
  
  
  
}
