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
    private String FROM_MAIL;

    /**
     * 메일 인증 요청을 확인하고 처리합니다.
     *
     * @param request 메일 인증 요청 객체
     */
    public void confirm(AuthMailRequest request) {
        authMailDomainService.confirm(request);
    }

    /**
     * 무작위 문자열 인증 코드를 생성합니다.
     *
     * @return 생성된 인증 코드 문자열
     */
    public String createCode() {
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
    public MimeMessage createMailForm(String toEmail, String authCode) throws MessagingException {
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
        message.setText(setContext(authCode), "utf-8", "html");

        return message;
    }

    /**
     * 주어진 이메일 주소와 관련된 인증 메일 정보를 삭제합니다.
     *
     * @param email 삭제할 이메일 주소
     */
    public void deleteByEmail(String email) {
        authMailDomainService.deleteByEmail(email);
    }


    /**
     * 주어진 이메일 주소와 인증 코드가 일치하는 인증 메일 정보가 존재하는지 확인합니다.
     *
     * @param email 이메일 주소
     * @param code 인증 코드
     * @return 인증 메일 정보가 존재하면 true, 그렇지 않으면 false
     */
    public boolean existsByEmailAndCode(String email, String code) {
        return authMailDomainService.existsByEmailAndCode(email, code);
    }

    /**
     * 주어진 이메일 주소에 대한 인증 상태를 확인합니다.
     *
     * @param email 확인할 이메일 주소
     * @return 이메일이 인증되었다면 true, 그렇지 않으면 false
     * @throws EntityNotFoundException 이메일에 대한 정보를 찾을 수 없는 경우 발생
     */
    public boolean isEmailAuthenticated(String email) throws EntityNotFoundException {
        return authMailDomainService.isEmailAuthenticated(email);
    }

    /**
     * Kafka 메시지를 수신하여 이메일을 보내는 메서드입니다.
     *
     * @param authMailRequest 인증 메일 요청 객체
     * @throws MessagingException 이메일 전송 중에 발생하는 예외
     */
    @KafkaListener(topics = "authMail")
    public void send(AuthMailRequest authMailRequest) throws MessagingException {
        MimeMessage emailForm = createMailForm(authMailRequest.getEmail(), authMailRequest.getCode());
        emailSender.send(emailForm);
    }

    /**
     * 인증 코드를 이메일 템플릿에 설정하고 렌더링된 이메일 본문을 반환합니다.
     *
     * @param code 설정할 인증 코드
     * @return 렌더링된 이메일 본문 문자열
     */
    public String setContext(String code) {
        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process("/web/mail", context);
    }

    /**
     * 인증 메일 요청 정보를 저장합니다.
     *
     * @param authMailRequest 저장할 인증 메일 요청 객체
     */
    public void save(AuthMailRequest authMailRequest) {
        authMailDomainService.save(authMailRequest.getEmail(), authMailRequest.getCode());
    }
}
