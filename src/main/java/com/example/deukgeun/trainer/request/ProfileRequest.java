package com.example.deukgeun.trainer.request;

import com.example.deukgeun.trainer.entity.Profile;
import lombok.Data;

@Data
public class ProfileRequest {
    private Long id;

    private Long userId;

    private String path;

    public static Profile create(String path, Long userId) {
        return Profile
                .builder()
                .path(path)
                .userId(userId)
                .build();
    }
}
