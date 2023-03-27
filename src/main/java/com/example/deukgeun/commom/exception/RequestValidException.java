package com.example.deukgeun.commom.exception;

import java.util.Map;

public class RequestValidException extends RuntimeException{

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  final Map<String, String> result;
  
  public RequestValidException(Map<String, String> result, String message) {
    super(message);
    this.result = result;
  }
  
  public Map<String, String> getResult() {
    return result;
  }
}
