package com.example.deukgeun.authMail.domain.dto;

import lombok.Data;

@Data
public class ConfirmDTO {
    private String email;
    private String code;

}
