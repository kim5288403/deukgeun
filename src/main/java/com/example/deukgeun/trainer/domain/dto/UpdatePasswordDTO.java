package com.example.deukgeun.trainer.domain.dto;

import lombok.Data;

@Data
public class UpdatePasswordDTO {
    private String email;
    private String password;
    private String newPassword;
}
