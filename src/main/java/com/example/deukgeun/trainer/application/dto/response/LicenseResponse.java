package com.example.deukgeun.trainer.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
public class LicenseResponse {
  @Data
  public static class List {
    private Long licenseId;

    private String certificateName;

    private String licenseNumber;

    private LocalDateTime createdDate;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Result {

    private Boolean result;

    private String certificatename;

    private String no;
  }
}
