package com.example.deukgeun.trainer.service.application;

import com.example.deukgeun.trainer.application.dto.request.RemoveLicenseRequest;
import com.example.deukgeun.trainer.application.dto.response.LicenseResponse;
import com.example.deukgeun.trainer.application.service.implement.LicenseApplicationServiceImpl;
import com.example.deukgeun.trainer.domain.dto.SaveLicenseDTO;
import com.example.deukgeun.trainer.domain.model.aggregate.Trainer;
import com.example.deukgeun.trainer.domain.model.entity.License;
import com.example.deukgeun.trainer.domain.service.TrainerDomainService;
import com.example.deukgeun.trainer.infrastructure.persistence.mapper.LicenseMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
public class LicenseApplicationServiceTest {

    @InjectMocks
    private LicenseApplicationServiceImpl licenseApplicationService;
    @Mock
    private TrainerDomainService trainerDomainService;
    @Mock
    private LicenseMapper licenseMapper;

    @Test
    public void givenValidEmailAndRemoveLicenseRequest_whenDeleteLicenseByLicenseId_thenLicenseDeleteCalled() {
        // Given
        String email = "email";
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);
        RemoveLicenseRequest removeLicenseRequest = new RemoveLicenseRequest();
        removeLicenseRequest.setIds(ids);

        // When
        licenseApplicationService.deleteLicenseByEmailAndLicenseId(email, removeLicenseRequest);

        // Then
        verify(trainerDomainService, times(ids.size())).deleteLicenseByEmailAndLicenseId(anyString(), anyLong());
    }

    @Test
    public void givenValidId_whenGetLicensesById_thenReturnLicenseResponseList() {
        // Given
        Long id = 1L;
        Trainer trainer = mock(Trainer.class);
        List<License> list = new ArrayList<>();
        list.add(License.create( "test1", "test1", id));
        list.add(License.create( "test2", "test2", id));

        given(trainer.getId()).willReturn(id);
        given(trainer.getLicenses()).willReturn(list);
        given(trainerDomainService.findById(anyLong())).willReturn(trainer);

        // When
        List<LicenseResponse.List> result = licenseApplicationService.getLicensesById(id);

        // Then
        assertEquals(2, result.size());
        verify(trainerDomainService, times(1)).findById(anyLong());
    }

    @Test
    public void givenValidEmail_whenGetLicensesByEmail_thenReturnLicenseResponseList() {
        // Given
        Long id = 1L;
        String email = "email";
        Trainer trainer = mock(Trainer.class);
        List<License> list = new ArrayList<>();
        list.add(License.create( "test1", "test1", id));
        list.add(License.create( "test2", "test2", id));

        given(trainer.getEmail()).willReturn(email);
        given(trainer.getLicenses()).willReturn(list);
        given(trainerDomainService.findByEmail(anyString())).willReturn(trainer);

        // When
        List<LicenseResponse.List> result = licenseApplicationService.getLicensesByEmail(email);

        // Then
        assertEquals(2, result.size());
        verify(trainerDomainService, times(1)).findByEmail(anyString());
    }

    @Test
    public void givenValidEmailAndLicenseResult_whenSaveLicense_thenReturnTrainer() {
        // Given
        String email = "test@example.com";
        Trainer trainer = mock(Trainer.class);
        LicenseResponse.Result licenseResult = mock(LicenseResponse.Result.class);
        SaveLicenseDTO saveLicenseDTO = mock(SaveLicenseDTO.class);

        given(licenseMapper.toSaveLicenseDto(anyString(), any(LicenseResponse.Result.class))).willReturn(saveLicenseDTO);
        given(trainerDomainService.saveLicense(any(SaveLicenseDTO.class))).willReturn(trainer);

        // When
        Trainer result = licenseApplicationService.saveLicense(email, licenseResult);

        // Then
        assertNotNull(result);
        verify(trainerDomainService, times(1)).saveLicense(any(SaveLicenseDTO.class));
    }
}
