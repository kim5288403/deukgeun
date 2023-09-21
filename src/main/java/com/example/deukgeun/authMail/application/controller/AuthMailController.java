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

  /**
   * 이메일을 보내는 요청을 처리합니다.
   *
   * @param request 이메일 요청 객체 (유효성 검사를 수행합니다)
   * @param bindingResult 유효성 검사 결과를 저장하는 BindingResult 객체
   * @return 이메일 보내기 결과에 대한 ResponseEntity
   * @throws MessagingException 메일 전송 중에 발생하는 예외
   * @throws UnsupportedEncodingException 인코딩 관련 예외
   */
  @RequestMapping(method = RequestMethod.POST, path = "/send")
  public ResponseEntity<?> send(
      @Valid EmailRequest request,
      BindingResult bindingResult)
      throws MessagingException, UnsupportedEncodingException {

    String toEmail = request.getEmail();

    // 동일한 이메일 주소를 가진 이전 인증 메일 정보 삭제
    authMailApplicationService.deleteByEmail(toEmail);

    // 새로운 인증 코드 생성
    String authCode = authMailApplicationService.createCode();

    // 새로운 인증 메일 요청 객체 생성
    AuthMailRequest authMailRequest = new AuthMailRequest();
    authMailRequest.setCode(authCode);
    authMailRequest.setEmail(toEmail);

    // 인증 메일 요청 정보 저장
    authMailApplicationService.save(authMailRequest);

    // Kafka 를 사용하여 인증 메일 요청을 토픽에 전송
    kafkaTemplate.send("send", authMailRequest);

    return RestResponseUtil
    .ok("인증 메일 보내기 성공했습니다.", null);
  }

  /**
   * 메일 인증을 확인하고 처리하는 요청을 처리합니다.
   *
   * @param request 인증 메일 요청 객체 (유효성 검사를 수행합니다)
   * @param bindingResult 유효성 검사 결과를 저장하는 BindingResult 객체
   * @return 메일 인증 결과에 대한 ResponseEntity
   */
  @RequestMapping(method = RequestMethod.POST, path = "/confirm")
  public ResponseEntity<?> confirm(@Valid AuthMailRequest request, BindingResult bindingResult) {
    // 유효성 검사를 통과한 인증 메일 요청을 처리
    authMailApplicationService.confirm(request);

    return RestResponseUtil
        .ok("메일 인증 성공 했습니다.", null);
  }
}
