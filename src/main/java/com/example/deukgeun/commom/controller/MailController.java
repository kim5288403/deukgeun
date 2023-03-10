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
import com.example.deukgeun.commom.enums.StatusEnum;
import com.example.deukgeun.commom.request.AuthMailRequest;
import com.example.deukgeun.commom.request.EmailRequest;
import com.example.deukgeun.commom.response.MessageResponse;
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
    MessageResponse response = MessageResponse
        .builder()
        .data(authCode)
        .message("인증 메일 보내기 성공했습니다.")
        .code(StatusEnum.OK.getCode())
        .status(StatusEnum.OK.getStatus())
        .build();

    return ResponseEntity
        .ok()
        .body(response);
  }

  // 인증 이메일 확인
  @RequestMapping(method = RequestMethod.POST, path = "/confirm")
  public ResponseEntity<?> confirm(@Valid AuthMailRequest request, BindingResult bindingResult) {
    MessageResponse response = null;

    if (bindingResult.hasErrors()) {
      validateService.errorMessageHandling(bindingResult);
    }

    mailservice.updateMailStatus(request, MailStatus.Y);

    response = MessageResponse
        .builder()
        .code(StatusEnum.OK.getCode())
        .status(StatusEnum.OK.getStatus())
        .message("메일 인증 성공 했습니다.")
        .data(request)
        .build();

    return ResponseEntity
        .ok()
        .body(response);
  }
}
