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
import com.example.deukgeun.commom.service.implement.MailServiceImpl;
import com.example.deukgeun.commom.service.implement.ValidateServiceImpl;
import com.example.deukgeun.commom.util.RestResponseUtil;
import lombok.RequiredArgsConstructor;

@RestController()
@RequestMapping("/mail")
@RequiredArgsConstructor
public class MailController {

  private final MailServiceImpl mailService;
  private final ValidateServiceImpl validateService;

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

    validateService.requestValidExceptionHandling(bindingResult);

    mailService.sendMail(request.getEmail());

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

    if (bindingResult.hasErrors()) {
      validateService.requestValidExceptionHandling(bindingResult);
    }

    mailService.updateMailStatus(request, MailStatus.Y);

    return RestResponseUtil
        .ok("메일 인증 성공 했습니다.", null);
  }
}
