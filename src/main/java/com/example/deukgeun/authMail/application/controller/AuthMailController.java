package com.example.deukgeun.authMail.application.controller;

import com.example.deukgeun.authMail.application.dto.request.AuthMailRequest;
import com.example.deukgeun.authMail.application.dto.request.EmailRequest;
import com.example.deukgeun.authMail.application.service.AuthMailApplicationService;
import com.example.deukgeun.global.util.RestResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

@RestController()
@RequestMapping("/api/authMail")
@RequiredArgsConstructor
public class AuthMailController {

  private final AuthMailApplicationService authMailApplicationService;
  private final KafkaTemplate<String, AuthMailRequest> kafkaTemplate;

  @RequestMapping(method = RequestMethod.POST, path = "/send")
  public ResponseEntity<?> send(
      @Valid EmailRequest request,
      BindingResult bindingResult)
      throws MessagingException, UnsupportedEncodingException {

    String toEmail = request.getEmail();

    authMailApplicationService.deleteByEmail(toEmail);

    String authCode = authMailApplicationService.createCode();

    AuthMailRequest authMailRequest = new AuthMailRequest();
    authMailRequest.setCode(authCode);
    authMailRequest.setEmail(toEmail);

    authMailApplicationService.save(authMailRequest);

    kafkaTemplate.send("authMail", authMailRequest);

    return RestResponseUtil
    .ok("인증 메일 보내기 성공했습니다.", null);
  }

  @RequestMapping(method = RequestMethod.POST, path = "/confirm")
  public ResponseEntity<?> confirm(@Valid AuthMailRequest request, BindingResult bindingResult) {

    authMailApplicationService.confirm(request);

    return RestResponseUtil
        .ok("메일 인증 성공 했습니다.", null);
  }
}
