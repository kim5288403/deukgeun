package com.example.deukgeun.auth.application.service.implement;

import com.example.deukgeun.auth.application.service.PasswordEncoderService;
import com.example.deukgeun.global.exception.PasswordMismatchException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordEncoderServiceImpl implements PasswordEncoderService {
    private final PasswordEncoder passwordEncoder;

    /**
     * 인증을 위해 제공된 비밀번호가 사용자의 비밀번호와 일치하는지 확인합니다.
     *
     * @param matchPassword 비교할 비밀번호
     * @param password 로그인 요청 비밀번호
     * @throws PasswordMismatchException 비밀번호가 사용자의 비밀번호와 일치하지 않는 경우 예외가 발생합니다.
     */
    public void isPasswordMatches(String password, String matchPassword) throws PasswordMismatchException {
        boolean check = passwordEncoder.matches(password, matchPassword);

        if (!check) {
            throw new PasswordMismatchException("사용자를 찾을 수 없습니다.");
        }
    }
}
