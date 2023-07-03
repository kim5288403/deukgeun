package com.example.deukgeun.trainer.controller;

import com.example.deukgeun.commom.response.RestResponse;
import com.example.deukgeun.commom.service.implement.TokenServiceImpl;
import com.example.deukgeun.commom.util.RestResponseUtil;
import com.example.deukgeun.trainer.request.RemoveLicenseRequest;
import com.example.deukgeun.trainer.request.SaveLicenseRequest;
import com.example.deukgeun.trainer.response.LicenseListResponse;
import com.example.deukgeun.trainer.response.LicenseResultResponse;
import com.example.deukgeun.trainer.service.implement.LicenseServiceImpl;
import com.example.deukgeun.trainer.service.implement.MemberServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class LicenseTest {
    @InjectMocks
    private LicenseController licenseController;
    @Mock
    private MemberServiceImpl memberService;
    @Mock
    private LicenseServiceImpl licenseService;
    @Mock
    private TokenServiceImpl tokenService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private BindingResult bindingResult;

    @Test
    public void givenLicenseService_whenGetListById_thenReturnResponseEntityWithLicenseList() {
        // Given
        Long memberId = 123L;
        List<LicenseListResponse> mockResponse = new ArrayList<>();
        given(licenseService.findByMemberId(memberId)).willReturn(mockResponse);
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("자격증 조회 성공했습니다.", mockResponse);

        // When
        ResponseEntity<?> responseEntity = licenseController.getListById(memberId);

        // Then
        verify(licenseService, times(1)).findByMemberId(memberId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
    }

    @Test
    public void givenTokenServiceAndMemberService_whenGetListByAuthToken_thenReturnResponseEntityWithLicenseList() {
        // Given
        String authToken = "exampleAuthToken";
        Long memberId = 123L;
        List<LicenseListResponse> mockResponse = new ArrayList<>();
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("자격증 조회 성공했습니다.", mockResponse);
        given(tokenService.resolveAuthToken(request)).willReturn(authToken);
        given(memberService.getUserId(authToken)).willReturn(memberId);
        given(licenseService.findByMemberId(memberId)).willReturn(mockResponse);

        // When
        ResponseEntity<?> responseEntity = licenseController.getListByAuthToken(request);

        // Then
        verify(tokenService, times(1)).resolveAuthToken(request);
        verify(memberService, times(1)).getUserId(authToken);
        verify(licenseService, times(1)).findByMemberId(memberId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
    }

    @Test
    public void givenTokenServiceMemberServiceAndLicenseService_whenSaveLicense_thenReturnResponseEntity() throws Exception {
        // Given
        SaveLicenseRequest saveLicenseRequest = new SaveLicenseRequest();
        LicenseResultResponse licenseResult = new LicenseResultResponse();
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("자격증 등록 성공했습니다.", null);

        String authToken = "exampleAuthToken";
        Long userId = 123L;

        given(tokenService.resolveAuthToken(request)).willReturn(authToken);
        given(memberService.getUserId(authToken)).willReturn(userId);
        given(licenseService.getLicenseVerificationResult(saveLicenseRequest)).willReturn(licenseResult);

        // When
        ResponseEntity<?> responseEntity = licenseController.save(request, saveLicenseRequest, bindingResult);

        // Then
        verify(licenseService).getLicenseVerificationResult(saveLicenseRequest);
        verify(licenseService).checkLicense(licenseResult);
        verify(tokenService).resolveAuthToken(request);
        verify(memberService).getUserId(authToken);
        verify(licenseService).save(licenseResult, userId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
    }

    @Test
    public void givenLicenseService_whenDeleteLicenses_thenReturnResponseEntity() {
        // Given
        RemoveLicenseRequest request = new RemoveLicenseRequest();
        List<Long> licenseIds = Arrays.asList(1L, 2L, 3L);
        request.setIds(licenseIds);
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("자격증 삭제 성공했습니다.", null);

        // When
        ResponseEntity<?> responseEntity = licenseController.delete(request, bindingResult);

        // Then
        licenseIds.forEach(id -> verify(licenseService).delete(id));
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
    }
}
