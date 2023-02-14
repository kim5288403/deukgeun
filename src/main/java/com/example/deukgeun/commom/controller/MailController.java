package com.example.deukgeun.commom.controller;

import java.io.UnsupportedEncodingException;
import javax.mail.MessagingException;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.example.deukgeun.commom.enums.MailStatus;
import com.example.deukgeun.commom.enums.StatusEnum;
import com.example.deukgeun.commom.exception.RequestValidException;
import com.example.deukgeun.commom.request.AuthMailRequest;
import com.example.deukgeun.commom.request.EmailRequest;
import com.example.deukgeun.commom.response.Message;
import com.example.deukgeun.commom.service.implement.MailServiceImpl;
import com.example.deukgeun.commom.service.implement.ValidateServiceImpl;

@RestController()
@RequestMapping("/mail")
public class MailController {
  
  @Autowired
  private MailServiceImpl mailservice;
  
  @Autowired
  private ValidateServiceImpl validateService;
  
  //인증 이메일 보내기
  @RequestMapping(method = RequestMethod.POST, path = "/send")
  public ResponseEntity<?> send(@Valid EmailRequest request, BindingResult bindingResult) {
    try {
      if (bindingResult.hasErrors()) {
        validateService.errorMessageHandling(bindingResult);
      }
      
      String authCode = mailservice.sendMail(request.getEmail());
      Message response = Message
          .builder()
          .data(authCode)
          .message("인증 메일 보내기 성공했습니다.")
          .code(StatusEnum.OK.getCode())
          .status(StatusEnum.OK.getStatus())
          .build();
      
      return ResponseEntity
          .ok()
          .body(response);
    } catch (UnsupportedEncodingException | MessagingException e) {
 
      return ResponseEntity
          .badRequest()
          .body(e.getMessage());
    } catch (RequestValidException e) {
      
      return ResponseEntity
          .badRequest()
          .body(e.getResponse());
    }
  }
  
  //구현할 내용
  //- 프로젝트 내에 애러 메세지 핸들링 수정
  //- 인증메일 확인 error view 작업
  
  //인증 이메일 확인
  @RequestMapping(method = RequestMethod.POST, path = "/confirm")
  public ResponseEntity<?> confirm(@Valid AuthMailRequest request, BindingResult bindingResult) {
    Message response = null;
    
    try {
      if (bindingResult.hasErrors()) {
        validateService.errorMessageHandling(bindingResult);
      }
      
      mailservice.updateMailStatus(request, MailStatus.Y);
        
      response = Message
          .builder()
          .code(StatusEnum.OK.getCode())
          .status(StatusEnum.OK.getStatus())
          .message("메일 인증 성공 했습니다.")
          .data(request)
          .build();
        
      return ResponseEntity
          .ok()
          .body(response);
    } catch (RequestValidException e) {
      
      return ResponseEntity
          .badRequest()
          .body(e.getResponse());
    } 
  }

}
