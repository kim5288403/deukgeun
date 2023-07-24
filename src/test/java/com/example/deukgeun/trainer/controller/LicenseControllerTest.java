package com.example.deukgeun.trainer.controller;

import com.example.deukgeun.authToken.application.dto.response.RestResponse;
import com.example.deukgeun.authToken.application.service.implement.AuthTokenApplicationServiceImpl;
import com.example.deukgeun.global.enums.Gender;
import com.example.deukgeun.global.util.RestResponseUtil;
import com.example.deukgeun.trainer.application.controller.LicenseController;
import com.example.deukgeun.trainer.application.dto.request.RemoveLicenseRequest;
import com.example.deukgeun.trainer.application.dto.request.SaveLicenseRequest;
import com.example.deukgeun.trainer.application.dto.response.LicenseListResponse;
import com.example.deukgeun.trainer.application.dto.response.LicenseResultResponse;
import com.example.deukgeun.trainer.application.service.implement.LicenseApplicationServiceImpl;
import com.example.deukgeun.trainer.application.service.implement.TrainerApplicationServiceImpl;
import com.example.deukgeun.trainer.domain.model.entity.Trainer;
import com.example.deukgeun.trainer.domain.model.valueobjcet.GroupStatus;
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
    private TrainerApplicationServiceImpl trainerApplicationService;
    @Mock
    private LicenseApplicationServiceImpl licenseApplicationService;
    @Mock
    private AuthTokenApplicationServiceImpl authTokenApplicationService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private BindingResult bindingResult;

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
        licenseIds.forEach(id -> verify(licenseApplicationService).deleteById(id));
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
    }

    @Test
    public void givenLicenseService_whenGetListById_thenReturnResponseEntityWithLicenseList() {
        // Given
        Long trainerId = 123L;
        List<LicenseListResponse> mockResponse = new ArrayList<>();
        given(licenseApplicationService.findByTrainerId(trainerId)).willReturn(mockResponse);
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("자격증 조회 성공했습니다.", mockResponse);

        // When
        ResponseEntity<?> responseEntity = licenseController.getListById(trainerId);

        // Then
        verify(licenseApplicationService, times(1)).findByTrainerId(trainerId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
    }

    @Test
    public void givenTokenServiceAndTrainerService_whenGetListByAuthToken_thenReturnResponseEntityWithLicenseList() {
        // Given
        String authToken = "exampleAuthToken";
        String email = "email";
        Long trainerId = 123L;
        Trainer trainer = new Trainer(
                trainerId,
                "test",
                email,
                "test",
                GroupStatus.N,
                "test",
                "test",
                "test",
                "test",
                "test",
                "test",
                Gender.M,
                3000,
                "test"
        );
        List<LicenseListResponse> mockResponse = new ArrayList<>();
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("자격증 조회 성공했습니다.", mockResponse);
        given(authTokenApplicationService.resolveAuthToken(request)).willReturn(authToken);
        given(authTokenApplicationService.getUserPk(authToken)).willReturn(email);
        given(trainerApplicationService.findByEmail(email)).willReturn(trainer);
        given(licenseApplicationService.findByTrainerId(trainerId)).willReturn(mockResponse);

        // When
        ResponseEntity<?> responseEntity = licenseController.getListByAuthToken(request);

        // Then
        verify(authTokenApplicationService, times(1)).resolveAuthToken(request);
        verify(authTokenApplicationService, times(1)).getUserPk(authToken);
        verify(trainerApplicationService, times(1)).findByEmail(email);
        verify(licenseApplicationService, times(1)).findByTrainerId(trainerId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
    }

    @Test
    public void givenTokenServiceTrainerServiceAndLicenseService_whenSaveLicense_thenReturnResponseEntity() throws Exception {
        // Given
        SaveLicenseRequest saveLicenseRequest = new SaveLicenseRequest();
        LicenseResultResponse licenseResult = new LicenseResultResponse();
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("자격증 등록 성공했습니다.", null);
        String email = "email";
        Long trainerId = 123L;
        Trainer trainer = new Trainer(
                trainerId,
                "test",
                email,
                "test",
                GroupStatus.N,
                "test",
                "test",
                "test",
                "test",
                "test",
                "test",
                Gender.M,
                3000,
                "test"
        );
        String authToken = "exampleAuthToken";


        given(authTokenApplicationService.resolveAuthToken(request)).willReturn(authToken);
        given(authTokenApplicationService.getUserPk(authToken)).willReturn(email);
        given(trainerApplicationService.findByEmail(email)).willReturn(trainer);
        given(licenseApplicationService.getLicenseVerificationResult(saveLicenseRequest)).willReturn(licenseResult);

        // When
        ResponseEntity<?> responseEntity = licenseController.save(request, saveLicenseRequest, bindingResult);

        // Then
        verify(licenseApplicationService, times(1)).getLicenseVerificationResult(saveLicenseRequest);
        verify(licenseApplicationService, times(1)).checkLicense(licenseResult);
        verify(authTokenApplicationService, times(1)).resolveAuthToken(request);
        verify(authTokenApplicationService, times(1)).getUserPk(authToken);
        verify(trainerApplicationService, times(1)).findByEmail(email);
        verify(licenseApplicationService, times(1)).save(licenseResult, trainerId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
    }

}
