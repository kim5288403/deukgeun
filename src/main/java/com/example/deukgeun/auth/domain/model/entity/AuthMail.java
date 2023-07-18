package com.example.deukgeun.auth.domain.model.entity;

import com.example.deukgeun.auth.domain.model.valueobject.MailStatus;
import com.example.deukgeun.global.util.LongIdGeneratorUtil;
import lombok.Getter;

@Getter
public class AuthMail {
    private Long id;

    private String email;

    private String code;

    private MailStatus mailStatus;

    public AuthMail(Long id, String email, String code, MailStatus mailStatus) {
        this.id = id;
        this.email = email;
        this.code = code;
        this.mailStatus = mailStatus;
    }

    public static AuthMail create(String email, String code) {
        Long id = LongIdGeneratorUtil.gen();
        return new AuthMail(id, email, code, MailStatus.N);
    }

    public void updateMailStatus(MailStatus mailStatus) {
        this.mailStatus = mailStatus;
    }

}
