package com.example.deukgeun.commom.exception;

import java.util.Map;
import com.example.deukgeun.commom.enums.StatusEnum;
import com.example.deukgeun.commom.response.Message;

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
  
  public Message getResponse() {
    return Message
        .builder()
        .code(StatusEnum.BAD_REQUEST.getCode())
        .status(StatusEnum.BAD_REQUEST.getStatus())
        .data(result)
        .message(getMessage())
        .build();
  }

  
}
