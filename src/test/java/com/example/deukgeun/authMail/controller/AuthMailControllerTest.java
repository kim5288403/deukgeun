package com.example.deukgeun.authMail.controller;

import com.example.deukgeun.authMail.application.controller.AuthMailController;
import com.example.deukgeun.authMail.application.dto.request.AuthMailRequest;
import com.example.deukgeun.authMail.application.dto.request.EmailRequest;
import com.example.deukgeun.authMail.application.service.AuthMailApplicationService;
import com.example.deukgeun.authToken.application.dto.response.RestResponse;
import com.example.deukgeun.global.util.RestResponseUtil;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.validation.BindingResult;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class AuthMailControllerTest {
    @InjectMocks
    private AuthMailController authMailController;
    @Mock
    private AuthMailApplicationService authMailApplicationService;
    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;
    @Mock
    private BindingResult bindingResult;

    @Test
    public void givenValidEmailRequest_WhenSend_ThenSendEmailAndReturnOkResponse() throws MessagingException, UnsupportedEncodingException {
        // Given
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setEmail("dummyEmail");
        String toEmail = emailRequest.getEmail();
        String authCode = "dummyAuthCode";
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("인증 메일 보내기 성공했습니다.", null);
        given(authMailApplicationService.createCode()).willReturn(authCode);

        // When
        ResponseEntity<?> responseEntity = authMailController.send(emailRequest, bindingResult);

        // Then
        verify(authMailApplicationService, times(1)).deleteByEmail(toEmail);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(expectedResponse.getBody());
    }

    @Test
    public void givenValidAuthMailRequest_WhenConfirm_ThenCallMailServiceAndReturnOkResponse() {
        // Given
        AuthMailRequest authMailRequest = new AuthMailRequest();
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("메일 인증 성공 했습니다.", null);

        // When
        ResponseEntity<?> responseEntity = authMailController.confirm(authMailRequest, bindingResult);

        // Then
        verify(authMailApplicationService).confirm(authMailRequest);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(expectedResponse.getBody());
    }

}
