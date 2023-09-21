package com.example.deukgeun.trainer.controller;

import com.example.deukgeun.authToken.application.service.AuthTokenApplicationService;
import com.example.deukgeun.global.dto.RestResponse;
import com.example.deukgeun.global.util.RestResponseUtil;
import com.example.deukgeun.trainer.application.controller.LicenseController;
import com.example.deukgeun.trainer.application.dto.request.RemoveLicenseRequest;
import com.example.deukgeun.trainer.application.dto.request.SaveLicenseRequest;
import com.example.deukgeun.trainer.application.dto.response.LicenseResponse;
import com.example.deukgeun.trainer.application.service.LicenseApplicationService;
import com.example.deukgeun.trainer.infrastructure.api.LicenseOpenApiService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
public class LicenseControllerTest {
    @InjectMocks
    private LicenseController licenseController;
    @Mock
    private LicenseApplicationService licenseApplicationService;
    @Mock
    private AuthTokenApplicationService authTokenApplicationService;
    @Mock
    private LicenseOpenApiService licenseOpenApiService;
    @Mock
    private HttpServletRequest request;

    @Test
    public void givenValidTrainerId_whenGetLicensesById_thenReturnSuccessResponse() {
        // Given
        Long id = 1L;
        LicenseResponse.List license1 = mock(LicenseResponse.List.class);
        LicenseResponse.List license2 = mock(LicenseResponse.List.class);

        List<LicenseResponse.List> licenses = new ArrayList<>();
        licenses.add(license1);
        licenses.add(license2);

        given(licenseApplicationService.getLicensesById(anyLong())).willReturn(licenses);

        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("자격증 조회 성공했습니다.", licenses);

        // When
        ResponseEntity<?> responseEntity = licenseController.getLicensesById(id);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
        verify(licenseApplicationService, times(1)).getLicensesById(anyLong());
    }

    @Test
    public void givenValidAndAuthToken_whenGetLicensesByAuthToken_thenReturnSuccessResponse() {
        // Given
        String authToken = "someAuthToken";
        String email = "example@example.com";

        LicenseResponse.List license1 = mock(LicenseResponse.List.class);
        LicenseResponse.List license2 = mock(LicenseResponse.List.class);

        List<LicenseResponse.List> licenses = new ArrayList<>();
        licenses.add(license1);
        licenses.add(license2);

        given(authTokenApplicationService.resolveAuthToken(any(HttpServletRequest.class))).willReturn(authToken);
        given(authTokenApplicationService.getUserPk(anyString())).willReturn(email);
        given(licenseApplicationService.getLicensesByEmail(anyString())).willReturn(licenses);
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("자격증 조회 성공했습니다.", licenses);

        // When
        ResponseEntity<?> responseEntity = licenseController.getLicensesByAuthToken(request);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
        verify(authTokenApplicationService, times(1)).resolveAuthToken(any(HttpServletRequest.class));
        verify(authTokenApplicationService, times(1)).getUserPk(anyString());
        verify(licenseApplicationService, times(1)).getLicensesByEmail(anyString());
    }

    @Test
    public void givenValidSaveLicenseRequest_whenSaveLicense_thenReturnSuccessResponse() {
        // Given
        String authToken = "someAuthToken";
        String email = "example@example.com";
        SaveLicenseRequest saveLicenseRequest = mock(SaveLicenseRequest.class);
        LicenseResponse.Result licenseResult = mock(LicenseResponse.Result.class);

        given(licenseOpenApiService.getLicenseVerificationResult(any(SaveLicenseRequest.class))).willReturn(licenseResult);
        given(authTokenApplicationService.resolveAuthToken(any(HttpServletRequest.class))).willReturn(authToken);
        given(authTokenApplicationService.getUserPk(anyString())).willReturn(email);

        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("자격증 등록 성공했습니다.", null);

        // When
        ResponseEntity<?> responseEntity = licenseController.saveLicense(request, saveLicenseRequest, null);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
        verify(licenseOpenApiService, times(1)).getLicenseVerificationResult(any(SaveLicenseRequest.class));
        verify(authTokenApplicationService, times(1)).resolveAuthToken(any(HttpServletRequest.class));
        verify(licenseApplicationService, times(1)).saveLicense(anyString(), any());
    }

    @Test
    public void givenValidRemoveLicenseRequest_whenDeleteLicense_thenReturnSuccessResponse() {
        // Given
        String authToken = "someAuthToken";
        String email = "example@example.com";
        RemoveLicenseRequest removeLicenseRequest = mock(RemoveLicenseRequest.class);

        given(authTokenApplicationService.resolveAuthToken(any(HttpServletRequest.class))).willReturn(authToken);
        given(authTokenApplicationService.getUserPk(anyString())).willReturn(email);

        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("자격증 삭제 성공했습니다.", null);

        // When
        ResponseEntity<?> responseEntity = licenseController.deleteLicense(request, removeLicenseRequest, null);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
        verify(licenseApplicationService, times(1)).deleteLicenseByEmailAndLicenseId(anyString(), any(RemoveLicenseRequest.class));
    }

}
