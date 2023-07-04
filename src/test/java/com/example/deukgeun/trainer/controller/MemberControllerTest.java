package com.example.deukgeun.trainer.controller;

import com.example.deukgeun.commom.enums.Gender;
import com.example.deukgeun.commom.response.LoginResponse;
import com.example.deukgeun.commom.response.RestResponse;
import com.example.deukgeun.commom.service.implement.TokenServiceImpl;
import com.example.deukgeun.commom.util.RestResponseUtil;
import com.example.deukgeun.trainer.entity.Member;
import com.example.deukgeun.trainer.entity.Profile;
import com.example.deukgeun.trainer.request.*;
import com.example.deukgeun.trainer.response.MemberResponse;
import com.example.deukgeun.trainer.service.implement.MemberServiceImpl;
import com.example.deukgeun.trainer.service.implement.ProfileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
public class MemberControllerTest {
    @InjectMocks
    private MemberController memberController;
    @Mock
    private ProfileServiceImpl profileService;
    @Mock
    private MemberServiceImpl memberService;
    @Mock
    private TokenServiceImpl tokenService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private BindingResult bindingResult;
    @Mock
    private HttpServletResponse response;

    @BeforeEach
    void setup () {
        ReflectionTestUtils.setField(memberController, "role", "trainer");
    }

    @Test
    void givenKeywordAndCurrentPage_whenGetList_thenReturnSuccessResponse() {
        // Given
        String keyword = "search keyword";
        Integer currentPage = 1;
        List<MemberResponse.MemberListResponse> memberList = new ArrayList<>();
        Page<MemberResponse.MemberListResponse> page = new PageImpl<>(memberList);
        MemberResponse.UserListPaginationResponse list = new MemberResponse.UserListPaginationResponse(page, currentPage);
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("조회 성공 했습니다.", list);

        given(memberService.getList(keyword, currentPage)).willReturn(page);

        // When
        ResponseEntity<?> responseEntity = memberController.getList(keyword, currentPage);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
        verify(memberService, times(1)).getList(keyword, currentPage);
    }

    @Test
    void givenValidAuthToken_whenGetDetail_thenReturnSuccessResponse() {
        // Given
        String authToken = "validAuthToken";
        Member expectedMember = Member
                .builder()
                .name("test")
                .price(3000)
                .jibunAddress("test")
                .detailAddress("test")
                .roadAddress("test")
                .gender(Gender.M)
                .groupName("")
                .build();
        MemberResponse response = new MemberResponse(expectedMember);
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("마이 페이지 조회 성공했습니다.", response);

        given(tokenService.resolveAuthToken(request)).willReturn(authToken);
        given(memberService.getByAuthToken(authToken)).willReturn(expectedMember);

        // When
        ResponseEntity<?> responseEntity = memberController.getDetail(request);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());

        verify(tokenService, times(1)).resolveAuthToken(request);
        verify(memberService, times(1)).getByAuthToken(authToken);
    }

    @Test
    void givenValidJoinRequest_whenSave_thenReturnSuccessResponse() throws IOException, IOException {
        // Given
        Member savedMember = Member
                .builder()
                .name("test")
                .price(3000)
                .jibunAddress("test")
                .detailAddress("test")
                .roadAddress("test")
                .gender(Gender.M)
                .groupName("")
                .build();

        JoinRequest joinRequest = mock(JoinRequest.class);
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("회원 가입 성공 했습니다.", null);

        given(memberService.save(joinRequest)).willReturn(savedMember);

        // When
        ResponseEntity<?> responseEntity = memberController.save(joinRequest, bindingResult);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());

        verify(memberService).save(joinRequest);
        verify(profileService).save(joinRequest.getProfile(), savedMember.getId());
    }

    @Test
    void givenValidUpdateInfoRequest_whenUpdate_thenReturnSuccessResponse() {
        // Given
        UpdateInfoRequest updateInfoRequest = mock(UpdateInfoRequest.class);
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("내 정보 수정 성공했습니다.", null);

        // When
        ResponseEntity<?> responseEntity = memberController.update(updateInfoRequest, null);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
        verify(memberService).updateInfo(updateInfoRequest);
    }

    @Test
    void givenValidUpdatePasswordRequest_whenUpdatePassword_thenReturnSuccessResponse() {
        // Given
        UpdatePasswordRequest updatePasswordRequest = mock(UpdatePasswordRequest.class);
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("비밀번호 변경 성공했습니다.", null);

        // When
        ResponseEntity<?> responseEntity = memberController.updatePassword(updatePasswordRequest, null);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
        verify(memberService).updatePassword(updatePasswordRequest);
    }

    @Test
    void givenValidWithdrawalUserRequest_whenWithdrawal_thenReturnSuccessResponse() throws IOException {
        // Given
        String authToken = "validAuthToken";
        WithdrawalUserRequest withdrawalUserRequest = mock(WithdrawalUserRequest.class);
        Member member = new Member();
        Profile userProfile = new Profile();
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("회원 탈퇴 성공했습니다.", null);

        given(memberService.getByEmail(withdrawalUserRequest.getEmail())).willReturn(member);
        given(profileService.getByMemberId(member.getId())).willReturn(userProfile);
        given(tokenService.resolveAuthToken(request)).willReturn(authToken);

        // When
        ResponseEntity<?> responseEntity = memberController.withdrawal(request, withdrawalUserRequest, null);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());

        verify(profileService, times(1)).deleteFileToDirectory(userProfile.getPath());
        verify(profileService, times(1)).withdrawal(userProfile.getId());
        verify(memberService, times(1)).withdrawal(member.getId());
        verify(tokenService, times(1)).resolveAuthToken(request);
        verify(tokenService, times(1)).deleteToken(anyString());
    }

    @Test
    void givenValidLoginRequest_whenLogin_thenReturnSuccessResponse() {
        // Given
        String authToken = "validAuthToken";
        LoginRequest loginRequest =  mock(LoginRequest.class);
        LoginResponse loginResponse = LoginResponse
                .builder()
                .authToken(authToken)
                .role("trainer")
                .build();

        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("로그인 성공 했습니다.", loginResponse);

        given(tokenService.setToken(loginRequest.getEmail(), response)).willReturn(authToken);

        // When
        ResponseEntity<?> responseEntity = memberController.login(loginRequest, bindingResult, response);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
        verify(memberService, times(1)).isPasswordMatches(loginRequest);
        verify(tokenService, times(1)).setToken(loginRequest.getEmail(), response);
    }
}
