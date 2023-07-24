package com.example.deukgeun.trainer.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LicenseListResponse {
  private Long licenseId;

  private String certificateName;
  
  private String licenseNumber;
  
  private LocalDateTime createdDate;
}
