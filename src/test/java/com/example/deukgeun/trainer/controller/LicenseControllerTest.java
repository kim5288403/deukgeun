package com.example.deukgeun.trainer.controller;

import com.example.deukgeun.authToken.application.dto.response.RestResponse;
import com.example.deukgeun.authToken.application.service.implement.AuthTokenApplicationServiceImpl;
import com.example.deukgeun.global.util.RestResponseUtil;
import com.example.deukgeun.trainer.application.controller.LicenseController;
import com.example.deukgeun.trainer.application.dto.request.RemoveLicenseRequest;
import com.example.deukgeun.trainer.application.dto.request.SaveLicenseRequest;
import com.example.deukgeun.trainer.application.dto.response.LicenseResponse;
import com.example.deukgeun.trainer.application.service.implement.TrainerApplicationServiceImpl;
import com.example.deukgeun.trainer.infrastructure.persistence.api.LicenseOpenApiService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class LicenseControllerTest {
    @InjectMocks
    private LicenseController licenseController;
    @Mock
    private TrainerApplicationServiceImpl trainerApplicationService;
    @Mock
    private AuthTokenApplicationServiceImpl authTokenApplicationService;
    @Mock
    private LicenseOpenApiService licenseOpenApiService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private BindingResult bindingResult;

    @Test
    public void givenTrainerId_whenGetLicensesById_thenReturnLicenseList() {
        // Given
        Long id = 1L;

        LicenseResponse.List license1 = new LicenseResponse.List(101L, "Certificate 1", "Test", LocalDateTime.now());
        LicenseResponse.List license2 = new LicenseResponse.List(102L, "Certificate 2", "Test", LocalDateTime.now());

        List<LicenseResponse.List> licenses = new ArrayList<>();
        licenses.add(license1);
        licenses.add(license2);

        given(trainerApplicationService.getLicensesById(id)).willReturn(licenses);
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("자격증 조회 성공했습니다.", licenses);

        // When
        ResponseEntity<?> responseEntity = licenseController.getLicensesById(id);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
        verify(trainerApplicationService, times(1)).getLicensesById(id);
    }

    @Test
    public void givenAuthTokenInRequest_whenGetLicensesByAuthToken_thenReturnLicenseList() {
        // Given
        String authToken = "someAuthToken";
        String email = "example@example.com";

        Long id = 1L;

        LicenseResponse.List license1 = new LicenseResponse.List(101L, "Certificate 1", "Test", LocalDateTime.now());
        LicenseResponse.List license2 = new LicenseResponse.List(102L, "Certificate 2", "Test", LocalDateTime.now());

        List<LicenseResponse.List> licenses = new ArrayList<>();
        licenses.add(license1);
        licenses.add(license2);

        given(authTokenApplicationService.resolveAuthToken(request)).willReturn(authToken);
        given(authTokenApplicationService.getUserPk(authToken)).willReturn(email);
        given(trainerApplicationService.getLicensesByEmail(email)).willReturn(licenses);
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("자격증 조회 성공했습니다.", licenses);

        // When
        ResponseEntity<?> responseEntity = licenseController.getLicensesByAuthToken(request);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
        verify(authTokenApplicationService, times(1)).resolveAuthToken(request);
        verify(authTokenApplicationService, times(1)).getUserPk(authToken);
        verify(trainerApplicationService, times(1)).getLicensesByEmail(email);
    }

    @Test
    public void givenValidSaveLicenseRequest_whenSaveLicense_thenReturnOkResponse() throws Exception {
        // Given
        String authToken = "someAuthToken";
        String email = "example@example.com";

        SaveLicenseRequest saveLicenseRequest = new SaveLicenseRequest();
        saveLicenseRequest.setCertificateName("CertificateName");
        saveLicenseRequest.setNo("123456");

        LicenseResponse.Result licenseResult = new LicenseResponse.Result(true, saveLicenseRequest.getCertificateName(), saveLicenseRequest.getNo());

        given(licenseOpenApiService.getLicenseVerificationResult(saveLicenseRequest)).willReturn(licenseResult);
        given(authTokenApplicationService.resolveAuthToken(request)).willReturn(authToken);
        given(authTokenApplicationService.getUserPk(authToken)).willReturn(email);
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("자격증 등록 성공했습니다.", null);


        // When
        ResponseEntity<?> responseEntity = licenseController.saveLicense(request, saveLicenseRequest, null);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
        verify(licenseOpenApiService, times(1)).getLicenseVerificationResult(saveLicenseRequest);
        verify(authTokenApplicationService, times(1)).resolveAuthToken(request);
        verify(trainerApplicationService, times(1)).saveLicense(email, licenseResult);
    }

    @Test
    public void givenValidRemoveLicenseRequest_whenDeleteLicense_thenLicensesShouldBeDeleted() {
        // Given
        String authToken = "someAuthToken";
        String email = "example@example.com";

        // RemoveLicenseRequest 객체를 생성합니다.
        RemoveLicenseRequest removeLicenseRequest = new RemoveLicenseRequest();
        removeLicenseRequest.setIds(Arrays.asList(101L, 102L, 103L));

        given(authTokenApplicationService.resolveAuthToken(request)).willReturn(authToken);
        given(authTokenApplicationService.getUserPk(authToken)).willReturn(email);
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("자격증 삭제 성공했습니다.", null);

        // When
        ResponseEntity<?> responseEntity = licenseController.deleteLicense(request, removeLicenseRequest, null);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
        verify(trainerApplicationService, times(removeLicenseRequest.getIds().size()))
                .deleteLicenseByLicenseId(anyString(), anyLong());
    }

}
