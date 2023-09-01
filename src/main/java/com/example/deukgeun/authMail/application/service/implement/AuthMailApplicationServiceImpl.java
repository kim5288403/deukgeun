package com.example.deukgeun.authMail.application.service.implement;

import com.example.deukgeun.authMail.application.dto.request.AuthMailRequest;
import com.example.deukgeun.authMail.application.service.AuthMailApplicationService;
import com.example.deukgeun.authMail.domain.service.AuthMailDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityNotFoundException;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthMailApplicationServiceImpl implements AuthMailApplicationService {

    private final JavaMailSender emailSender;
    private final SpringTemplateEngine templateEngine;
    private final AuthMailDomainService authMailDomainService;

    @Value("${spring.mail.username}")
    private String fromEmail; // 보내는 이메일

    public void confirm(AuthMailRequest request) {
        authMailDomainService.confirm(request);
    }

    public String createCode() {
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(3);

            switch (index) {
                case 0:
                    // 소문자 알파벳
                    key.append((char) (random.nextInt(26) + 97));
                    break;
                case 1:
                    // 대문자 알파벳
                    key.append((char) (random.nextInt(26) + 65));
                    break;
                case 2:
                    // 숫자
                    key.append(random.nextInt(9));
                    break;
            }
        }

        return key.toString();
    }

    public MimeMessage createMailForm(String toEmail, String authCode) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, toEmail);
        String title = "득근득근 회원가입 인증 번호";
        message.setSubject(title);
        message.setFrom(fromEmail);
        message.setText(setContext(authCode), "utf-8", "html");

        return message;
    }

    public void deleteByEmail(String email) {
        authMailDomainService.deleteByEmail(email);
    }

    public boolean existsByEmailAndCode(String email, String code) {
        return authMailDomainService.existsByEmailAndCode(email, code);
    }

    public boolean isEmailAuthenticated(String email) throws EntityNotFoundException {
        return authMailDomainService.isEmailAuthenticated(email);
    }

    @KafkaListener(topics = "authMail")
    public void send(AuthMailRequest authMailRequest) throws MessagingException {
        MimeMessage emailForm = createMailForm(authMailRequest.getEmail(), authMailRequest.getCode());
        emailSender.send(emailForm);
    }

    public String setContext(String code) {
        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process("/web/mail", context);
    }

    public void save(AuthMailRequest authMailRequest) {
        authMailDomainService.save(authMailRequest.getEmail(), authMailRequest.getCode());
    }
}
