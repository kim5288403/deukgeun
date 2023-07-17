package com.example.deukgeun.auth.application.service;

public interface PasswordEncoderService {
    void isPasswordMatches(String password, String matchPassword);
}
