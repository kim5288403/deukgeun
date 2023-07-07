package com.example.deukgeun.global.exception;

import java.util.Map;

public class RequestValidException extends RuntimeException{

  private static final long serialVersionUID = 1L;

  final Map<String, String> result;

  /**
   *  파라미터 result 를 해당 객체 result 에 set 함
   *  파라미터 message 를 부모 클래스 message 에 set 함
   *
   * @param result request validator 에 애러 내용
   * @param message error 메세지
   */
  public RequestValidException(Map<String, String> result, String message) {
    super(message);
    this.result = result;
  }

  /**
   *
   * @return 해당 객체 result 를 반환
   */
  public Map<String, String> getResult() {
    return result;
  }
}
