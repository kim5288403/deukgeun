package com.example.deukgeun.global.util;

import com.example.deukgeun.global.exception.PasswordMismatchException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


public class PasswordEncoderUtil {

    /**
     * 두 개의 패스워드가 일치하는지 확인합니다.
     *
     * @param password      비교할 패스워드입니다.
     * @param matchPassword 일치 여부를 확인할 패스워드입니다.
     * @throws PasswordMismatchException 두 패스워드가 일치하지 않을 경우 예외를 발생시킵니다.
     */
    public static void isPasswordMatches(String password, String matchPassword) throws PasswordMismatchException {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean check = passwordEncoder.matches(password, matchPassword);

        if (!check) {
            throw new PasswordMismatchException("사용자를 찾을 수 없습니다.");
        }
    }

    /**
     * 주어진 패스워드를 BCrypt 알고리즘을 사용하여 해시화합니다.
     *
     * @param password 해시화할 패스워드입니다.
     * @return 해시화된 패스워드 문자열입니다.
     */
    public static String encode(String password) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }
}
