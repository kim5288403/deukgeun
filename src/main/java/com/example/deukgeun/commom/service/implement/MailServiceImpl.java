package com.example.deukgeun.commom.service.implement;

import java.io.UnsupportedEncodingException;
import java.util.Random;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import com.example.deukgeun.commom.entity.AuthMail;
import com.example.deukgeun.commom.enums.MailStatus;
import com.example.deukgeun.commom.repository.MailRepository;
import com.example.deukgeun.commom.request.AuthMailRequest;
import com.example.deukgeun.commom.service.MailService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService{
  
  @Autowired
  private MailRepository mailRepository;
  
  //의존성 주입을 통해서 필요한 객체를 가져온다.
  private final JavaMailSender emailSender;
  // 타임리프를사용하기 위한 객체를 의존성 주입으로 가져온다
  private final SpringTemplateEngine templateEngine;
  //랜덤 인증 코드
  private String authCode; 
  //보내는 이메일
  private String fromEmail = "kim5288403@gmail.com";
  //메일 제목
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

      createCode(); //인증 코드 생성

      MimeMessage message = emailSender.createMimeMessage();
      message.addRecipients(MimeMessage.RecipientType.TO, toEmail); //보낼 이메일 설정
      message.setSubject(title); //제목 설정
      message.setFrom(fromEmail); //보내는 이메일
      message.setText(setContext(authCode), "utf-8", "html");

      return message;
  }

  //실제 메일 전송
  public String sendMail(String toEmail) throws MessagingException, UnsupportedEncodingException {
  
      //메일전송에 필요한 정보 설정
      MimeMessage emailForm = createMailForm(toEmail);
      //실제 메일 전송
      emailSender.send(emailForm);
      
      AuthMail authMail = AuthMailRequest.create(toEmail, authCode);
      createAuthMail(authMail);
      
      return authCode; //인증 코드 반환
  }

  //타임리프를 이용한 context 설정
  public String setContext(String code) {
      Context context = new Context();
      context.setVariable("code", code);
      return templateEngine.process("/web/mail", context); //mail.html
  }
  
  //인증 메일 정보 저장
  public void createAuthMail(AuthMail authMail) {
    mailRepository.save(authMail);
  }
  
  
  //메일 인증 확인
  public boolean confirmMail(AuthMailRequest request) {
    return mailRepository.existsByEmailAndCode(request.getEmail(), request.getCode());
  }
  
  public void updateMailStatus(AuthMailRequest request, MailStatus status) {
    mailRepository.updateStatusByEmailAndCode(request.getEmail(), request.getCode(), status);
  }

  
  
}
