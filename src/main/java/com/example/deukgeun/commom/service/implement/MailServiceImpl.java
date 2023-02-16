package com.example.deukgeun.commom.service.implement;

import java.io.UnsupportedEncodingException;
import java.util.Random;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import com.example.deukgeun.commom.entity.AuthMail;
import com.example.deukgeun.commom.enums.MailStatus;
import com.example.deukgeun.commom.repository.AuthMailRepository;
import com.example.deukgeun.commom.request.AuthMailRequest;
import com.example.deukgeun.commom.service.MailService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService{
  
  private final AuthMailRepository authMailRepository;
  private final JavaMailSender emailSender;
  private final SpringTemplateEngine templateEngine;
  
  private String authCode; 
  private String fromEmail = "kim5288403@gmail.com"; // 보내는 이메일
  private String title = "득근득근 회원가입 인증 번호";
  
  //랜덤 인증 코드 생성
  public void createCode() {
      Random random = new Random();
      StringBuffer key = new StringBuffer();

      for(int i=0;i<8;i++) {
          int index = random.nextInt(3);

          switch (index) {
              case 0 :
                  key.append((char) ((int)random.nextInt(26) + 97));
                  break;
              case 1:
                  key.append((char) ((int)random.nextInt(26) + 65));
                  break;
              case 2:
                  key.append(random.nextInt(9));
                  break;
          }
      }
      authCode = key.toString();
  }
  
  //메일 양식 작성
  public MimeMessage createMailForm(String toEmail) throws MessagingException, UnsupportedEncodingException {
      createCode();

      MimeMessage message = emailSender.createMimeMessage();
      message.addRecipients(MimeMessage.RecipientType.TO, toEmail);
      message.setSubject(title);
      message.setFrom(fromEmail);
      message.setText(setContext(authCode), "utf-8", "html");

      return message;
  }

  //실제 메일 전송
  public String sendMail(String toEmail) throws MessagingException, UnsupportedEncodingException {
      //중복 인증 정보 제거
      if(authMailRepository.existsByEmail(toEmail)) {
        deleteAuthMail(toEmail);
      }
      
      MimeMessage emailForm = createMailForm(toEmail);
      emailSender.send(emailForm);
      AuthMail authMail = AuthMailRequest.create(toEmail, authCode, MailStatus.N);
      createAuthMail(authMail);
      
      return authCode; //인증 코드 반환
  }

  //mail view 설정
  public String setContext(String code) {
      Context context = new Context();
      context.setVariable("code", code);
      return templateEngine.process("/web/mail", context); //mail.html
  }
  
  //인증 메일 정보 저장
  public void createAuthMail(AuthMail authMail) {
    authMailRepository.save(authMail);
  }
  
  //인증 메일 정보 삭제
  public void deleteAuthMail(String email) {
    authMailRepository.deleteByEmail(email);
  }
  
  //메일 인증 확인
  public boolean confirmMail(AuthMailRequest request) {
    return authMailRepository.existsByEmailAndCode(request.getEmail(), request.getCode());
  }
  
  //메일인증 상태 업데이트
  public void updateMailStatus(AuthMailRequest request, MailStatus status) {
    authMailRepository.updateStatusByEmailAndCode(request.getEmail(), request.getCode(), status);
  }

  
  
}
