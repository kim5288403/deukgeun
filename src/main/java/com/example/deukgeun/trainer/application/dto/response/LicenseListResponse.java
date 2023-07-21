package com.example.deukgeun.trainer.application.dto.response;

import java.time.LocalDateTime;
import com.example.deukgeun.trainer.infrastructure.persistence.entity.License;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LicenseListResponse {
  private Long licenseId;

  private String certificateName;
  
  private String licenseNumber;
  
  private LocalDateTime createdDate;
  
  public LicenseListResponse(License license) {
    this.licenseId =license.getId();
    this.certificateName = license.getCertificateName();
    this.licenseNumber = license.getLicenseNumber();
    this.createdDate = license.getCreatedDate();
  }
}
