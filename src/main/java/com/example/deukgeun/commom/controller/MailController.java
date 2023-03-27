package com.example.deukgeun.commom.controller;

import java.io.UnsupportedEncodingException;
import javax.mail.MessagingException;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.example.deukgeun.commom.enums.MailStatus;
import com.example.deukgeun.commom.request.AuthMailRequest;
import com.example.deukgeun.commom.request.EmailRequest;
import com.example.deukgeun.commom.response.RestResponseUtil;
import com.example.deukgeun.commom.service.implement.MailServiceImpl;
import com.example.deukgeun.commom.service.implement.ValidateServiceImpl;
import lombok.RequiredArgsConstructor;

@RestController()
@RequestMapping("/mail")
@RequiredArgsConstructor
public class MailController {

  private final MailServiceImpl mailservice;
  private final ValidateServiceImpl validateService;

  // 인증 이메일 보내기
  @RequestMapping(method = RequestMethod.POST, path = "/send")
  public ResponseEntity<?> send(
      @Valid EmailRequest request,
      BindingResult bindingResult)
      throws MessagingException, UnsupportedEncodingException {

    if (bindingResult.hasErrors()) {
      validateService.errorMessageHandling(bindingResult);
    }

    String authCode = mailservice.sendMail(request.getEmail());

    return new RestResponseUtil()
    .okResponse("인증 메일 보내기 성공했습니다.", authCode);
  }

  // 인증 이메일 확인
  @RequestMapping(method = RequestMethod.POST, path = "/confirm")
  public ResponseEntity<?> confirm(@Valid AuthMailRequest request, BindingResult bindingResult) {

    if (bindingResult.hasErrors()) {
      validateService.errorMessageHandling(bindingResult);
    }

    mailservice.updateMailStatus(request, MailStatus.Y);

    return new RestResponseUtil()
        .okResponse("메일 인증 성공 했습니다.", null);
  }
}
