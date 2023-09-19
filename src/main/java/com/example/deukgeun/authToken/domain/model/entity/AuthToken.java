package com.example.deukgeun.authToken.domain.model.entity;

import com.example.deukgeun.global.util.LongIdGeneratorUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthToken {
    private final Long id;

    private String authToken;

    private String refreshToken;

    public static AuthToken create(String authToken, String refreshToken) {
        Long id = LongIdGeneratorUtil.gen();
        return new AuthToken(id, authToken, refreshToken);
    }

    public void updateAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
