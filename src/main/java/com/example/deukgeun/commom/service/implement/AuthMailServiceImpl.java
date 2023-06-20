package com.example.deukgeun.commom.service.implement;

import com.example.deukgeun.commom.entity.AuthMail;
import com.example.deukgeun.commom.enums.MailStatus;
import com.example.deukgeun.commom.repository.AuthMailRepository;
import com.example.deukgeun.commom.request.AuthMailRequest;
import com.example.deukgeun.commom.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
public class AuthMailServiceImpl implements MailService {

    private final AuthMailRepository authMailRepository;
    private final JavaMailSender emailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${trainer.mail.email}")
    private String fromEmail; // 보내는 이메일

    private String title = "득근득근 회원가입 인증 번호";

    /**
     * 랜덤한 인증 코드를 생성합니다.
     * 생성된 인증 코드는 클래스 변수 authCode 에 저장됩니다.
     *
     * @return 인증 코드
     */
    public String createCode() {
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(3);

            switch (index) {
                case 0:
                    // 소문자 알파벳
                    key.append((char) ((int) random.nextInt(26) + 97));
                    break;
                case 1:
                    // 대문자 알파벳
                    key.append((char) ((int) random.nextInt(26) + 65));
                    break;
                case 2:
                    // 숫자
                    key.append(random.nextInt(9));
                    break;
            }
        }

        return key.toString();
    }

    /**
     * 수신자 이메일을 기반으로 MimeMessage 객체를 생성합니다.
     * 생성된 MimeMessage 에는 인증 코드를 포함한 이메일 내용이 설정됩니다.
     *
     * @param toEmail 수신자 이메일
     * @return 생성된 MimeMessage 객체
     * @throws MessagingException 메일 생성 중 발생한 예외
     */
    public MimeMessage createMailForm(String toEmail, String authCode) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, toEmail);
        message.setSubject(title);
        message.setFrom(fromEmail);
        message.setText(setContext(authCode), "utf-8", "html");

        return message;
    }

    /**
     * 주어진 이메일 주소로 메일을 전송합니다.
     *
     * @param toEmail 수신자 이메일 주소
     * @throws MessagingException           메일 전송 중 발생한 예외
     */
    public void send(String toEmail, String authCode) throws MessagingException {
        MimeMessage emailForm = createMailForm(toEmail, authCode);
        emailSender.send(emailForm);
    }

    /**
     * 주어진 코드를 이용하여 메일 내용을 생성합니다.
     *
     * @param code 인증 코드
     * @return 생성된 메일 내용
     */
    public String setContext(String code) {
        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process("/web/mail", context); //mail.html
    }

    /**
     * 이메일 주소를 제공하여 AuthMail 객체를 저장합니다.
     *
     * @param toEmail 저장할 이메일 주소
     */
    public void save(String toEmail, String authCode) {
        AuthMail authMail  = AuthMail
                .builder()
                .email(toEmail)
                .code(authCode)
                .mailStatus(MailStatus.N)
                .build();

        authMailRepository.save(authMail);
    }

    /**
     * 주어진 이메일에 대한 인증 메일 정보를 삭제합니다.
     *
     * @param email 삭제할 인증 메일의 이메일
     */
    public void deleteByEmail(String email) {
        if (authMailRepository.existsByEmail(email)) {
            authMailRepository.deleteByEmail(email);
        }
    }

    /**
     * 주어진 이메일과 코드를 사용하여 메일 인증을 확인합니다.
     *
     * @param email 메일 인증을 확인할 이메일
     * @param code  메일 인증 코드
     * @return 인증이 확인되면 true 를 반환하고, 그렇지 않으면 false 를 반환합니다.
     */
    public boolean confirmMail(String email, String code) {
        return authMailRepository.existsByEmailAndCode(email, code);
    }

    /**
     * 주어진 이메일이 인증된 이메일인지 확인합니다.
     *
     * @param email 인증 여부를 확인할 이메일
     * @return 이메일이 인증된 경우 true 를 반환하고, 그렇지 않으면 false 를 반환합니다.
     * @throws EntityNotFoundException 주어진 이메일에 해당하는 사용자를 찾을 수 없는 경우 발생하는 예외
     */
    public boolean isEmailAuthenticated(String email) throws EntityNotFoundException {
        AuthMail authMail = authMailRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        return authMail.getMailStatus() == MailStatus.Y;
    }

    /**
     * 주어진 이메일과 코드를 사용하여 메일 인증 정보의 상태를 업데이트합니다.
     *
     * @param request 업데이트할 메일 인증 정보의 이메일과 코드
     */
    public void confirm(AuthMailRequest request) {
        AuthMail findAuthMail = authMailRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        findAuthMail.updateMailStatus(MailStatus.Y);
        authMailRepository.save(findAuthMail);

    }
}
