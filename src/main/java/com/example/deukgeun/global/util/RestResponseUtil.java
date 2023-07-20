package com.example.deukgeun.global.util;

import com.example.deukgeun.global.enums.StatusEnum;
import com.example.deukgeun.authToken.application.dto.response.RestResponse;
import org.springframework.http.ResponseEntity;

public class RestResponseUtil {

  public static ResponseEntity<RestResponse> ok(String message, Object data){
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
  
  public static ResponseEntity<RestResponse> bad(String message, Object data){
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

  public static ResponseEntity<RestResponse> FORBIDDEN(String message, Object data){
    RestResponse response = RestResponse
            .builder()
            .code(StatusEnum.FORBIDDEN.getCode())
            .status(StatusEnum.FORBIDDEN.getStatus())
            .data(data)
            .message(message)
            .build();

    return ResponseEntity.status(StatusEnum.FORBIDDEN.getCode())
            .body(response);
  }
}
