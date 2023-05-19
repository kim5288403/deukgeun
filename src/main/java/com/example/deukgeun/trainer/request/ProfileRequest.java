package com.example.deukgeun.trainer.request;

import com.example.deukgeun.trainer.entity.Profile;
import lombok.Builder;
import lombok.Data;

@Data
public class ProfileRequest {
    private Long id;

    private Long userId;

    private String path;

    @Builder
    public ProfileRequest(String path) {
        this.path = path;
    }

    public static Profile create(String fileName, Long userId) {
        return Profile
                .builder()
                .path(fileName)
                .userId(userId)
                .build();
    }
}
