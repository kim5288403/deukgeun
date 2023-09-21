package com.example.deukgeun.global.util;

import com.example.deukgeun.authMail.application.dto.request.AuthMailRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Random;

public class MailUtil {
    private static JavaMailSender emailSender;
    private static SpringTemplateEngine templateEngine;
    @Value("${spring.mail.username}")
    private static String FROM_MAIL;

    /**
     * 무작위 문자열 인증 코드를 생성합니다.
     *
     * @return 생성된 인증 코드 문자열
     */
    public static String createCode() {
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        // 8자리의 인증 코드를 생성합니다.
        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(3);

            switch (index) {
                case 0:
                    // 소문자 알파벳 (a-z) 중에서 무작위 문자 추가
                    key.append((char) (random.nextInt(26) + 97));
                    break;
                case 1:
                    // 대문자 알파벳 (A-Z) 중에서 무작위 문자 추가
                    key.append((char) (random.nextInt(26) + 65));
                    break;
                case 2:
                    // 0부터 9까지의 숫자 중에서 무작위 숫자 추가
                    key.append(random.nextInt(9));
                    break;
            }
        }

        return key.toString();
    }

    /**
     * 이메일 메시지를 생성합니다.
     *
     * @param toEmail 수신자 이메일 주소
     * @param authCode 생성된 인증 코드
     * @return 생성된 이메일 메시지 (MimeMessage)
     * @throws MessagingException 메시지 생성 중에 발생하는 예외
     */
    public static MimeMessage createMailForm(String toEmail, String authCode) throws MessagingException {
        // 빈 MimeMessage 생성
        MimeMessage message = emailSender.createMimeMessage();

        // 수신자 이메일 주소 추가
        message.addRecipients(MimeMessage.RecipientType.TO, toEmail);

        // 이메일 제목 설정
        String title = "득근득근 회원가입 인증 번호";
        message.setSubject(title);

        // 이메일 발신자 설정
        message.setFrom(FROM_MAIL);

        // 이메일 본문 설정 (HTML 형식)
        message.setText(MailUtil.setContext(authCode), "utf-8", "html");

        return message;
    }

    /**
     * Kafka 메시지를 수신하여 이메일을 보내는 메서드입니다.
     *
     * @param authMailRequest 인증 메일 요청 객체
     * @throws MessagingException 이메일 전송 중에 발생하는 예외
     */
    @KafkaListener(topics = "send")
    public static void send(AuthMailRequest authMailRequest) throws MessagingException {
        MimeMessage emailForm = createMailForm(authMailRequest.getEmail(), authMailRequest.getCode());
        emailSender.send(emailForm);
    }

    /**
     * 인증 코드를 이메일 템플릿에 설정하고 렌더링된 이메일 본문을 반환합니다.
     *
     * @param code 설정할 인증 코드
     * @return 렌더링된 이메일 본문 문자열
     */
    public static String setContext(String code) {
        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process("/web/mail", context);
    }
}
