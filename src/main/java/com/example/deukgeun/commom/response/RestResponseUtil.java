package com.example.deukgeun.commom.response;

import org.springframework.http.ResponseEntity;
import com.example.deukgeun.commom.enums.StatusEnum;

public class RestResponseUtil {
  
  public static ResponseEntity<RestResponse> okResponse(String message, Object data){
    RestResponse response = RestResponse
          .builder()
          .code(StatusEnum.OK.getCode())
          .status(StatusEnum.OK.getStatus())
          .data(data)
          .message(message)
          .build();
    
    return ResponseEntity.ok()
          .body(response);
  }
  
  public static ResponseEntity<RestResponse> BadResponse(String message, Object data){
    RestResponse response = RestResponse
          .builder()
          .code(StatusEnum.BAD_REQUEST.getCode())
          .status(StatusEnum.BAD_REQUEST.getStatus())
          .data(data)
          .message(message)
          .build();
    
    return ResponseEntity.badRequest()
          .body(response);
  }
}
