package com.example.deukgeun.trainer.domain.dto;

import lombok.Data;

@Data
public class SaveLicenseDTO {
    private String email;
    private Boolean result;
    private String certificatename;
    private String no;
}
