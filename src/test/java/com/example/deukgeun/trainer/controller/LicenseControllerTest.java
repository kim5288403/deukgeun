package com.example.deukgeun.trainer.controller;

import com.example.deukgeun.main.response.RestResponse;
import com.example.deukgeun.main.service.implement.TokenServiceImpl;
import com.example.deukgeun.global.util.RestResponseUtil;
import com.example.deukgeun.trainer.request.RemoveLicenseRequest;
import com.example.deukgeun.trainer.request.SaveLicenseRequest;
import com.example.deukgeun.trainer.response.LicenseListResponse;
import com.example.deukgeun.trainer.response.LicenseResultResponse;
import com.example.deukgeun.trainer.service.implement.LicenseServiceImpl;
import com.example.deukgeun.trainer.service.implement.TrainerServiceImpl;
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
public class LicenseControllerTest {
    @InjectMocks
    private LicenseController licenseController;
    @Mock
    private TrainerServiceImpl trainerService;
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
        Long trainerId = 123L;
        List<LicenseListResponse> mockResponse = new ArrayList<>();
        given(licenseService.findByTrainerId(trainerId)).willReturn(mockResponse);
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("자격증 조회 성공했습니다.", mockResponse);

        // When
        ResponseEntity<?> responseEntity = licenseController.getListById(trainerId);

        // Then
        verify(licenseService, times(1)).findByTrainerId(trainerId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
    }

    @Test
    public void givenTokenServiceAndTrainerService_whenGetListByAuthToken_thenReturnResponseEntityWithLicenseList() {
        // Given
        String authToken = "exampleAuthToken";
        Long trainerId = 123L;
        List<LicenseListResponse> mockResponse = new ArrayList<>();
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("자격증 조회 성공했습니다.", mockResponse);
        given(tokenService.resolveAuthToken(request)).willReturn(authToken);
        given(trainerService.getTrainerId(authToken)).willReturn(trainerId);
        given(licenseService.findByTrainerId(trainerId)).willReturn(mockResponse);

        // When
        ResponseEntity<?> responseEntity = licenseController.getListByAuthToken(request);

        // Then
        verify(tokenService, times(1)).resolveAuthToken(request);
        verify(trainerService, times(1)).getTrainerId(authToken);
        verify(licenseService, times(1)).findByTrainerId(trainerId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
    }

    @Test
    public void givenTokenServiceTrainerServiceAndLicenseService_whenSaveLicense_thenReturnResponseEntity() throws Exception {
        // Given
        SaveLicenseRequest saveLicenseRequest = new SaveLicenseRequest();
        LicenseResultResponse licenseResult = new LicenseResultResponse();
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("자격증 등록 성공했습니다.", null);

        String authToken = "exampleAuthToken";
        Long trainerId = 123L;

        given(tokenService.resolveAuthToken(request)).willReturn(authToken);
        given(trainerService.getTrainerId(authToken)).willReturn(trainerId);
        given(licenseService.getLicenseVerificationResult(saveLicenseRequest)).willReturn(licenseResult);

        // When
        ResponseEntity<?> responseEntity = licenseController.save(request, saveLicenseRequest, bindingResult);

        // Then
        verify(licenseService, times(1)).getLicenseVerificationResult(saveLicenseRequest);
        verify(licenseService, times(1)).checkLicense(licenseResult);
        verify(tokenService, times(1)).resolveAuthToken(request);
        verify(trainerService, times(1)).getTrainerId(authToken);
        verify(licenseService, times(1)).save(licenseResult, trainerId);
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
