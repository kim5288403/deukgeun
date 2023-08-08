package com.example.deukgeun.trainer.application.controller;

import com.example.deukgeun.authToken.application.service.implement.AuthTokenApplicationServiceImpl;
import com.example.deukgeun.global.util.RestResponseUtil;
import com.example.deukgeun.trainer.application.dto.request.RemoveLicenseRequest;
import com.example.deukgeun.trainer.application.dto.request.SaveLicenseRequest;
import com.example.deukgeun.trainer.application.dto.response.LicenseResponse;
import com.example.deukgeun.trainer.application.service.TrainerApplicationService;
import com.example.deukgeun.trainer.infrastructure.api.LicenseOpenApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/trainer/license")
@RequiredArgsConstructor
public class LicenseController {
    private final LicenseOpenApiService licenseOpenApiService;
    private final TrainerApplicationService trainerApplicationService;
    private final AuthTokenApplicationServiceImpl authTokenApplicationService;

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity<?> getLicensesById(@PathVariable Long id) {
        List<LicenseResponse.List> response = trainerApplicationService.getLicensesById(id);

        return RestResponseUtil
                .ok("자격증 조회 성공했습니다.", response);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/")
    public ResponseEntity<?> getLicensesByAuthToken(HttpServletRequest request) {
        String authToken = authTokenApplicationService.resolveAuthToken(request);
        String email = authTokenApplicationService.getUserPk(authToken);
        List<LicenseResponse.List> response = trainerApplicationService.getLicensesByEmail(email);

        return RestResponseUtil
                .ok("자격증 조회 성공했습니다.", response);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/")
    public ResponseEntity<?> saveLicense(HttpServletRequest request, @Valid SaveLicenseRequest saveLicenseRequest, BindingResult bindingResult) throws Exception {
        LicenseResponse.Result licenseResult = licenseOpenApiService.getLicenseVerificationResult(saveLicenseRequest);
        licenseResult.setCertificatename(saveLicenseRequest.getCertificateName());
        licenseResult.setNo(saveLicenseRequest.getNo());
        String authToken = authTokenApplicationService.resolveAuthToken(request);
        String email = authTokenApplicationService.getUserPk(authToken);

        trainerApplicationService.saveLicense(email, licenseResult);

        return RestResponseUtil
                .ok("자격증 등록 성공했습니다.", null);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/")
    public ResponseEntity<?> deleteLicense(HttpServletRequest request, @Valid RemoveLicenseRequest removeLicenseRequest, BindingResult bindingResult) {
        String authToken = authTokenApplicationService.resolveAuthToken(request);
        String email = authTokenApplicationService.getUserPk(authToken);

        removeLicenseRequest
                .getIds()
                .forEach(licenceId -> trainerApplicationService.deleteLicenseByLicenseId(email, licenceId));

        return RestResponseUtil
                .ok("자격증 삭제 성공했습니다.", null);
    }
}
