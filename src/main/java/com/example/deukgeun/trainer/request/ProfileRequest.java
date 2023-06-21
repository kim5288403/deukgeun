package com.example.deukgeun.trainer.request;

import lombok.Data;

@Data
public class ProfileRequest {
    private Long id;

    private Long userId;

    private String path;
}
