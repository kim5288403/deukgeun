package com.example.deukgeun.trainer.controller;

import com.example.deukgeun.authToken.application.dto.response.RestResponse;
import com.example.deukgeun.authToken.application.service.implement.AuthTokenApplicationServiceImpl;
import com.example.deukgeun.global.enums.Gender;
import com.example.deukgeun.global.util.RestResponseUtil;
import com.example.deukgeun.trainer.application.controller.LicenseController;
import com.example.deukgeun.trainer.application.dto.request.RemoveLicenseRequest;
import com.example.deukgeun.trainer.application.dto.request.SaveLicenseRequest;
import com.example.deukgeun.trainer.application.dto.response.LicenseResultResponse;
import com.example.deukgeun.trainer.application.service.implement.TrainerApplicationServiceImpl;
import com.example.deukgeun.trainer.domain.model.aggregate.Trainer;
import com.example.deukgeun.trainer.domain.model.entity.License;
import com.example.deukgeun.trainer.domain.model.valueobjcet.GroupStatus;
import com.example.deukgeun.trainer.infrastructure.persistence.api.LicenseOpenApiService;
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

        License license1 = new License(101L, "Certificate 1", "Test", id);
        License license2 = new License(102L, "Certificate 2", "Test", id);

        List<License> licenses = new ArrayList<>();
        licenses.add(license1);
        licenses.add(license2);

        Trainer trainer = new Trainer(
                id,
                "test",
                "email",
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
                "test",
                licenses
        );

        given(trainerApplicationService.findById(id)).willReturn(trainer);
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("자격증 조회 성공했습니다.", licenses);

        // When
        ResponseEntity<?> responseEntity = licenseController.getLicensesById(id);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
        verify(trainerApplicationService, times(1)).findById(id);
    }

    @Test
    public void givenAuthTokenInRequest_whenGetLicensesByAuthToken_thenReturnLicenseList() {
        // Given
        String authToken = "someAuthToken";
        String email = "example@example.com";

        Long id = 1L;

        License license1 = new License(101L, "Certificate 1", "Test", id);
        License license2 = new License(102L, "Certificate 2", "Test", id);

        List<License> licenses = new ArrayList<>();
        licenses.add(license1);
        licenses.add(license2);

        Trainer trainer = new Trainer(
                id,
                "test",
                "email",
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
                "test",
                licenses
        );

        given(authTokenApplicationService.resolveAuthToken(request)).willReturn(authToken);
        given(authTokenApplicationService.getUserPk(authToken)).willReturn(email);
        given(trainerApplicationService.findByEmail(email)).willReturn(trainer);
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("자격증 조회 성공했습니다.", licenses);

        // When
        ResponseEntity<?> responseEntity = licenseController.getLicensesByAuthToken(request);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
        verify(authTokenApplicationService, times(1)).resolveAuthToken(request);
        verify(authTokenApplicationService, times(1)).getUserPk(authToken);
        verify(trainerApplicationService, times(1)).findByEmail(email);
    }

    @Test
    public void givenValidSaveLicenseRequest_whenSaveLicense_thenReturnOkResponse() throws Exception {
        // Given
        String authToken = "someAuthToken";
        String email = "example@example.com";

        SaveLicenseRequest saveLicenseRequest = new SaveLicenseRequest();
        saveLicenseRequest.setName("CertificateName");
        saveLicenseRequest.setNo("123456");

        LicenseResultResponse licenseResult = new LicenseResultResponse(true, saveLicenseRequest.getName(), saveLicenseRequest.getNo());

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
