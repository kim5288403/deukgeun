package com.example.deukgeun.trainer.license;

import com.example.deukgeun.trainer.request.SaveLicenseRequest;
import com.example.deukgeun.trainer.service.implement.LicenseServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class CheckLicenseTest {
    @Autowired
    private LicenseServiceImpl licenseService;

    @Test
    void shouldIllegalArgumentExceptionForInvalidParameter() {
        // Given
        String name = "테스트 자격증";
        String no = "t1e2s3t4";
        SaveLicenseRequest request = new SaveLicenseRequest();
        request.setName(name);
        request.setNo(no);

        // When, Then
        assertThrows(IllegalArgumentException.class, () -> {
            licenseService.checkLicense(request);
        });
    }
}
