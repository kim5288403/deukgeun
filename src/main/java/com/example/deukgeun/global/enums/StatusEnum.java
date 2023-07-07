package com.example.deukgeun.global.enums;

public enum StatusEnum {
  OK(200, "OK"),
  NO_CONTENT(204, "NO_CONTENT"),
  BAD_REQUEST(400, "BAD_REQUEST"),
  FORBIDDEN(403, "FORBIDDEN"),
  NOT_FOUND(404, "NOT_FOUND"),
  INTERNAL_SERER_ERROR(500, "INTERNAL_SERVER_ERROR");

  int code;
  String status;

  StatusEnum(int code, String status) {
    this.code = code;
    this.status = status;
  }

  public int getCode() {
    return this.code;
  }

  public String getStatus() {
    return this.status;
  }
}
