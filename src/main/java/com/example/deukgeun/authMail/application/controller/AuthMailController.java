package com.example.deukgeun.authMail.application.controller;

import com.example.deukgeun.authMail.application.dto.request.AuthMailRequest;
import com.example.deukgeun.authMail.application.dto.request.EmailRequest;
import com.example.deukgeun.authMail.application.service.AuthMailApplicationService;
import com.example.deukgeun.global.util.RestResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

@RestController()
@RequestMapping("/api/mail")
@RequiredArgsConstructor
public class AuthMailController {

  private final AuthMailApplicationService authMailApplicationService;

  /**
   * 이메일을 보내는 메소드입니다.
   *
   * @param request        이메일 요청 객체
   * @param bindingResult  데이터 유효성 검사 결과
   * @return ResponseEntity<?> 응답 엔티티
   * @throws MessagingException          메일 전송 중 발생하는 예외
   * @throws UnsupportedEncodingException 인코딩 예외
   */
  @RequestMapping(method = RequestMethod.POST, path = "/send")
  public ResponseEntity<?> send(
      @Valid EmailRequest request,
      BindingResult bindingResult)
      throws MessagingException, UnsupportedEncodingException {

    String toEmail = request.getEmail();

    // 중복 인증 정보 제거
    authMailApplicationService.deleteByEmail(toEmail);

    // 인증 코드 생성
    String authCode = authMailApplicationService.createCode();

    // 메일 전송
    authMailApplicationService.send(request.getEmail(), authCode);

    // 인증 메일 정보 저장
    authMailApplicationService.save(toEmail, authCode);

    return RestResponseUtil
    .ok("인증 메일 보내기 성공했습니다.", null);
  }

  /**
   * 이메일 인증을 처리하는 메소드입니다.
   *
   * @param request        이메일 인증 요청 객체
   * @param bindingResult  데이터 유효성 검사 결과
   * @return ResponseEntity<?> 응답 엔티티
   */
  @RequestMapping(method = RequestMethod.POST, path = "/confirm")
  public ResponseEntity<?> confirm(@Valid AuthMailRequest request, BindingResult bindingResult) {

    authMailApplicationService.confirm(request);

    return RestResponseUtil
        .ok("메일 인증 성공 했습니다.", null);
  }
}
