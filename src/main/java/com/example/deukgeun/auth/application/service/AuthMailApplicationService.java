package com.example.deukgeun.auth.application.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public interface AuthMailApplicationService {
    String createCode();
    MimeMessage createMailForm(String toEmail, String authCode) throws MessagingException;
    void send(String toEmail, String authCode) throws MessagingException;
    String setContext(String code);
}
