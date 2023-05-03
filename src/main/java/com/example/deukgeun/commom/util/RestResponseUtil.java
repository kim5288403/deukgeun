package com.example.deukgeun.commom.util;

import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import com.example.deukgeun.commom.enums.StatusEnum;
import com.example.deukgeun.commom.response.RestResponse;

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
}
