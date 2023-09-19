package com.example.deukgeun.global.util;

import com.example.deukgeun.global.dto.RestResponse;
import com.example.deukgeun.global.enums.StatusEnum;
import org.springframework.http.ResponseEntity;

public class RestResponseUtil {

  /**
   * 성공 상태 (HTTP 200 OK)에 대한 응답을 생성합니다.
   *
   * @param message 응답 메시지입니다.
   * @param data    응답 데이터입니다.
   * @return ResponseEntity 객체로 래핑된 응답입니다.
   */
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

  /**
   * 실패 상태 (HTTP 400 Bad Request)에 대한 응답을 생성합니다.
   *
   * @param message 응답 메시지입니다.
   * @param data    응답 데이터입니다.
   * @return ResponseEntity 객체로 래핑된 응답입니다.
   */
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

  /**
   * 실패 상태 (HTTP 403 Forbidden)에 대한 응답을 생성합니다.
   *
   * @param message 응답 메시지입니다.
   * @param data    응답 데이터입니다.
   * @return ResponseEntity 객체로 래핑된 응답입니다.
   */
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
