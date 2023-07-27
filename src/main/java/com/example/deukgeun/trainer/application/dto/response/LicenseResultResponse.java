package com.example.deukgeun.trainer.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LicenseResultResponse {
  
  private Boolean result;
  
  private String certificatename;

  private String no;
  
}
