package com.example.deukgeun.global.exception.advice;

import com.example.deukgeun.global.exception.PasswordMismatchException;
import com.example.deukgeun.global.exception.RequestValidException;
import com.example.deukgeun.global.util.RestResponseUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.mail.MessagingException;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@RestControllerAdvice
public class RestControllerAdvisor {

  /**
   * 이 메서드는 Exception 클래스의 예외를 처리하고 클라이언트에 응답을 반환합니다.
   *
   * @param e 발생한 예외 객체입니다.
   * @return ResponseEntity 객체를 반환하여 클라이언트에 예외 정보를 응답합니다.
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> exceptionHandler(Exception e) {
    return RestResponseUtil
        .bad(e.getMessage(), null);
  }

  /**
   * 이 메서드는 EntityExistsException 예외를 처리하고 클라이언트에 응답을 반환합니다.
   *
   * @param e EntityExistsException 예외 객체입니다.
   * @return ResponseEntity 객체를 반환하여 클라이언트에 예외 정보를 응답합니다.
   */
  @ExceptionHandler(EntityExistsException.class)
  public ResponseEntity<?> entityExistsExceptionHandler(EntityExistsException e) {
    return RestResponseUtil
            .bad(e.getMessage(), null);
  }

  /**
   * 이 메서드는 PasswordMismatchException 예외를 처리하고 클라이언트에 응답을 반환합니다.
   *
   * @param e PasswordMismatchException 예외 객체입니다.
   * @return ResponseEntity 객체를 반환하여 클라이언트에 예외 정보를 응답합니다.
   */
  @ExceptionHandler(PasswordMismatchException.class)
  public ResponseEntity<?> passwordMismatchExceptionHandler(PasswordMismatchException e) {
    return RestResponseUtil
            .bad(e.getMessage(), null);
  }

  /**
   * 이 메서드는 EntityNotFoundException 예외를 처리하고 클라이언트에 응답을 반환합니다.
   *
   * @param e EntityNotFoundException 예외 객체입니다.
   * @return ResponseEntity 객체를 반환하여 클라이언트에 예외 정보를 응답합니다.
   */
  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<?> entityNotFoundExceptionHandler(EntityNotFoundException e) {
    return RestResponseUtil
            .bad(e.getMessage(), null);
  }

  /**
   * 이 메서드는 SignatureException 예외를 처리하고 클라이언트에 응답을 반환합니다.
   *
   * @param e SignatureException 예외 객체입니다.
   * @return ResponseEntity 객체를 반환하여 클라이언트에 예외 정보를 응답합니다.
   */
  @ExceptionHandler(SignatureException.class)
  public ResponseEntity<?> signatureExceptionHandler(SignatureException e) {
    return RestResponseUtil
            .bad("잘못된 JWT signature 형식입니다.", null);
  }

  /**
   * 이 메서드는 IOException 예외를 처리하고 클라이언트에 응답을 반환합니다.
   *
   * @param e IOException 예외 객체입니다.
   * @return ResponseEntity 객체를 반환하여 클라이언트에 예외 정보를 응답합니다.
   */
  @ExceptionHandler(IOException.class)
  public ResponseEntity<?> IoExceptionHandler(IOException e) {
    return RestResponseUtil
            .bad(e.getMessage(), null);
  }

  /**
   * 이 메서드는 RequestValidException 예외를 처리하고 클라이언트에 응답을 반환합니다.
   *
   * @param e RequestValidException 예외 객체입니다.
   * @return ResponseEntity 객체를 반환하여 클라이언트에 예외 정보와 결과 데이터를 응답합니다.
   */
  @ExceptionHandler(RequestValidException.class)
  public ResponseEntity<?> requestValidException(RequestValidException e) {
    
    return RestResponseUtil
        .bad(e.getMessage(), e.getResult());
  }

  /**
   * 이 메서드는 MalformedJwtException 예외를 처리하고 클라이언트에 응답을 반환합니다.
   *
   * @param e MalformedJwtException 예외 객체입니다.
   * @return ResponseEntity 객체를 반환하여 클라이언트에 예외 정보를 응답합니다.
   */
  @ExceptionHandler(MalformedJwtException.class)
  public ResponseEntity<?> malformedJwtException(MalformedJwtException e) {

    return RestResponseUtil
            .bad("JWT(Jason Web Token) 문자열이 올바른 형식이 아니어서 발생하는 예외입니다.", null);
  }

  /**
   * 이 메서드는 UnsupportedEncodingException 예외를 처리하고 클라이언트에 응답을 반환합니다.
   *
   * @param e UnsupportedEncodingException 예외 객체입니다.
   * @return ResponseEntity 객체를 반환하여 클라이언트에 예외 정보를 응답합니다.
   */
  @ExceptionHandler(UnsupportedEncodingException.class)
  public ResponseEntity<?> unsupportedEncodingException(UnsupportedEncodingException e) {
    return RestResponseUtil
        .bad(e.getMessage(), null);
  }

  /**
   * 이 메서드는 MessagingException 예외를 처리하고 클라이언트에 응답을 반환합니다.
   *
   * @param e MessagingException 예외 객체입니다.
   * @return ResponseEntity 객체를 반환하여 클라이언트에 예외 정보를 응답합니다.
   */
  @ExceptionHandler(MessagingException.class)
  public ResponseEntity<?> messagingException(MessagingException e) {
    return RestResponseUtil
        .bad(e.getMessage(), null);
  }

  /**
   * 이 메서드는 ValidationException 예외를 처리하고 클라이언트에 응답을 반환합니다.
   *
   * @param e ValidationException 예외 객체입니다.
   * @return ResponseEntity 객체를 반환하여 클라이언트에 예외 정보를 응답합니다.
   */
  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<?> validationException(ValidationException e) {
    return RestResponseUtil
        .bad(e.getMessage(), null);
  }

  /**
   * 이 메서드는 ExpiredJwtException 예외를 처리하고 클라이언트에 응답을 반환합니다.
   *
   * @param e ExpiredJwtException 예외 객체입니다.
   * @return ResponseEntity 객체를 반환하여 클라이언트에 예외 정보를 응답합니다.
   */
  @ExceptionHandler(ExpiredJwtException.class)
  public ResponseEntity<?> expiredJwtException(ExpiredJwtException e) {
    return RestResponseUtil
        .bad(e.getMessage(), null);
  }
}
