package com.example.deukgeun.member.controller;

import com.example.deukgeun.global.dto.RestResponse;
import com.example.deukgeun.global.util.RestResponseUtil;
import com.example.deukgeun.member.application.controller.MemberController;
import com.example.deukgeun.member.application.dto.request.JoinRequest;
import com.example.deukgeun.member.application.service.MemberApplicationService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class MemberControllerTest {
    @InjectMocks
    private MemberController memberController;
    @Mock
    private MemberApplicationService memberApplicationService;
    @Mock
    private BindingResult bindingResult;

    @Test
    void givenValidJoinRequest_whenSave_thenReturnSuccessResponse() {
        // Given
        JoinRequest joinRequest = mock(JoinRequest.class);

        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("회원 가입 성공 했습니다.", null);

        // When
        ResponseEntity<?> responseEntity = memberController.save(joinRequest, bindingResult);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
        verify(memberApplicationService, times(1)).save(any(JoinRequest.class));
    }
}
