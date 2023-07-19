package com.example.deukgeun.auth.domain.model.entity;

import com.example.deukgeun.global.util.LongIdGeneratorUtil;
import lombok.Getter;

@Getter
public class AuthToken {
    private Long id;

    private String authToken;

    private String refreshToken;

    public AuthToken (Long id, String authToken, String refreshToken) {
        this.id = id;
        this.authToken = authToken;
        this.refreshToken = refreshToken;
    }

    public static AuthToken create(String authToken, String refreshToken) {
        Long id = LongIdGeneratorUtil.gen();
        return new AuthToken(id, authToken, refreshToken);
    }

    public void updateAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
