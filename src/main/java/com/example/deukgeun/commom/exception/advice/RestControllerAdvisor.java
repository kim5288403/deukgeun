package com.example.deukgeun.commom.exception.advice;

import java.io.UnsupportedEncodingException;
import javax.mail.MessagingException;
import javax.validation.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.example.deukgeun.commom.exception.RequestValidException;
import io.jsonwebtoken.ExpiredJwtException;

@RestControllerAdvice
public class RestControllerAdvisor {
  
  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> exceptionHandler(Exception e) {
    return ResponseEntity
        .badRequest()
        .body(e.getMessage());
  }
  
  @ExceptionHandler(RequestValidException.class)
  public ResponseEntity<?> requestValidException(RequestValidException e) {
    return ResponseEntity
        .badRequest()
        .body(e.getResponse());
  }
  
  @ExceptionHandler(UnsupportedEncodingException.class)
  public ResponseEntity<?> unsupportedEncodingException(UnsupportedEncodingException e) {
    return ResponseEntity
        .badRequest()
        .body(e.getMessage());
  }
  
  @ExceptionHandler(MessagingException.class)
  public ResponseEntity<?> messagingException(MessagingException e) {
    return ResponseEntity
        .badRequest()
        .body(e.getMessage());
  }
  
  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<?> validationException(ValidationException e) {
    return ResponseEntity
        .badRequest()
        .body(e.getMessage());
  }
  
  @ExceptionHandler(ExpiredJwtException.class)
  public ResponseEntity<?> expiredJwtException(ExpiredJwtException e) {
    return ResponseEntity
        .badRequest()
        .body(e.getMessage());
  }
  
  
  
}
