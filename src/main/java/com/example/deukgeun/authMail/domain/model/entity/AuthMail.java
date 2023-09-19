package com.example.deukgeun.authMail.domain.model.entity;

import com.example.deukgeun.authMail.domain.model.valueobject.MailStatus;
import com.example.deukgeun.global.util.LongIdGeneratorUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthMail {
    private Long id;

    private String email;

    private String code;

    private MailStatus mailStatus;

    public static AuthMail create(String email, String code) {
        Long id = LongIdGeneratorUtil.gen();
        return new AuthMail(id, email, code, MailStatus.N);
    }

    public void updateMailStatus(MailStatus mailStatus) {
        this.mailStatus = mailStatus;
    }

    public boolean isEmailAuthenticated() {
        return this.mailStatus == MailStatus.Y;
    }

}
