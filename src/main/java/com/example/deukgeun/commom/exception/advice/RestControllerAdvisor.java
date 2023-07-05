package com.example.deukgeun.commom.exception.advice;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.mail.MessagingException;
import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;

import com.example.deukgeun.commom.exception.PasswordMismatchException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
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
        .bad(e.getMessage(), null);
  }

  @ExceptionHandler(PasswordMismatchException.class)
  public ResponseEntity<?> passwordMismatchExceptionHandler(PasswordMismatchException e) {
    return RestResponseUtil
            .bad(e.getMessage(), null);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<?> entityNotFoundExceptionHandler(EntityNotFoundException e) {
    return RestResponseUtil
            .bad(e.getMessage(), null);
  }



  @ExceptionHandler(SignatureException.class)
  public ResponseEntity<?> signatureExceptionHandler(SignatureException e) {
    return RestResponseUtil
            .bad("잘못된 JWT signature 형식입니다.", null);
  }

  @ExceptionHandler(IOException.class)
  public ResponseEntity<?> IoExceptionHandler(IOException e) {
    return RestResponseUtil
            .bad(e.getMessage(), null);
  }
  
  @ExceptionHandler(RequestValidException.class)
  public ResponseEntity<?> requestValidException(RequestValidException e) {
    
    return RestResponseUtil
        .bad(e.getMessage(), e.getResult());
  }

  @ExceptionHandler(MalformedJwtException.class)
  public ResponseEntity<?> malformedJwtException(MalformedJwtException e) {

    return RestResponseUtil
            .bad("JWT(Jason Web Token) 문자열이 올바른 형식이 아니어서 발생하는 예외입니다.", null);
  }
  
  @ExceptionHandler(UnsupportedEncodingException.class)
  public ResponseEntity<?> unsupportedEncodingException(UnsupportedEncodingException e) {
    return RestResponseUtil
        .bad(e.getMessage(), null);
  }
  
  @ExceptionHandler(MessagingException.class)
  public ResponseEntity<?> messagingException(MessagingException e) {
    return RestResponseUtil
        .bad(e.getMessage(), null);
  }
  
  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<?> validationException(ValidationException e) {
    return RestResponseUtil
        .bad(e.getMessage(), null);
  }
  
  @ExceptionHandler(ExpiredJwtException.class)
  public ResponseEntity<?> expiredJwtException(ExpiredJwtException e) {
    return RestResponseUtil
        .bad(e.getMessage(), null);
  }
  
  
  
}
