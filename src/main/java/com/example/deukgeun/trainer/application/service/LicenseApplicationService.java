package com.example.deukgeun.trainer.application.service;

import com.example.deukgeun.trainer.application.dto.request.RemoveLicenseRequest;
import com.example.deukgeun.trainer.application.dto.response.LicenseResponse;
import com.example.deukgeun.trainer.domain.model.aggregate.Trainer;

import java.util.List;

public interface LicenseApplicationService {
    void deleteLicenseByEmailAndLicenseId(String email, RemoveLicenseRequest removeLicenseRequest);
    List<LicenseResponse.List> getLicensesById(Long id);
    List<LicenseResponse.List> getLicensesByEmail(String email);
    Trainer saveLicense(String email, LicenseResponse.Result licenseResult);
}
