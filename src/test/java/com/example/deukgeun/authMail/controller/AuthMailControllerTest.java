package com.example.deukgeun.authMail.controller;

import com.example.deukgeun.authMail.application.controller.AuthMailController;
import com.example.deukgeun.authMail.application.dto.request.AuthMailRequest;
import com.example.deukgeun.authMail.application.dto.request.EmailRequest;
import com.example.deukgeun.authMail.application.service.AuthMailApplicationService;
import com.example.deukgeun.global.dto.RestResponse;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AuthMailControllerTest {
    @InjectMocks
    private AuthMailController authMailController;
    @Mock
    private AuthMailApplicationService authMailApplicationService;
    @Mock
    private KafkaTemplate<String, AuthMailRequest> kafkaTemplate;
    @Mock
    private BindingResult bindingResult;

    @Test
    public void givenValidEmailRequest_whenSend_thenReturnSuccessResponse() throws MessagingException, UnsupportedEncodingException {
        // Given
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setEmail("dummyEmail");
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("인증 메일 보내기 성공했습니다.", null);

        given(authMailApplicationService.createCode()).willReturn("createCode");

        // When
        ResponseEntity<?> responseEntity = authMailController.send(emailRequest, bindingResult);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(expectedResponse.getBody());
        verify(authMailApplicationService, times(1)).deleteByEmail(anyString());
        verify(authMailApplicationService, times(1)).createCode();
        verify(authMailApplicationService, times(1)).save(any(AuthMailRequest.class));
        verify(kafkaTemplate, times(1)).send(anyString(), any(AuthMailRequest.class));
    }

    @Test
    public void givenValidAuthMailRequest_whenConfirm_thenReturnSuccessResponse() {
        // Given
        AuthMailRequest authMailRequest = mock(AuthMailRequest.class);
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("메일 인증 성공 했습니다.", null);

        // When
        ResponseEntity<?> responseEntity = authMailController.confirm(authMailRequest, bindingResult);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(expectedResponse.getBody());
        verify(authMailApplicationService, times(1)).confirm(any(AuthMailRequest.class));
    }

}
