package com.example.deukgeun.trainer.application.dto.response;

import com.example.deukgeun.trainer.domain.model.entity.License;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class LicenseResponse {
  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class List {
    private Long licenseId;

    private String certificateName;

    private String licenseNumber;

    private LocalDateTime createdDate;

    public List (License license) {
      this.licenseId = license.getId();
      this.certificateName = license.getCertificateName();
      this.licenseNumber = license.getLicenseNumber();
      this.createdDate = license.getCreatedDate();
    }
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
