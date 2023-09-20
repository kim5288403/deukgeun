package com.example.deukgeun.trainer.application.controller;

import com.example.deukgeun.authToken.application.service.implement.AuthTokenApplicationServiceImpl;
import com.example.deukgeun.global.util.RestResponseUtil;
import com.example.deukgeun.trainer.application.dto.request.RemoveLicenseRequest;
import com.example.deukgeun.trainer.application.dto.request.SaveLicenseRequest;
import com.example.deukgeun.trainer.application.dto.response.LicenseResponse;
import com.example.deukgeun.trainer.application.service.LicenseApplicationService;
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
    private final LicenseApplicationService licenseApplicationService;
    private final AuthTokenApplicationServiceImpl authTokenApplicationService;

    /**
     * 특정 ID에 해당하는 트레이너의 자격증 목록을 조회합니다.
     *
     * @param id 조회할 트레이너의 ID입니다.
     * @return 자격증 목록을 나타내는 ResponseEntity 객체입니다.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity<?> getLicensesById(@PathVariable Long id) {
        List<LicenseResponse.List> response = licenseApplicationService.getLicensesById(id);

        return RestResponseUtil.ok("자격증 조회 성공했습니다.", response);
    }

    /**
     * 현재 로그인한 사용자의 자격증 목록을 조회합니다.
     *
     * @param request HTTP 요청 객체입니다. 인증 토큰을 추출하기 위해 사용됩니다.
     * @return 자격증 목록을 나타내는 ResponseEntity 객체입니다.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/")
    public ResponseEntity<?> getLicensesByAuthToken(HttpServletRequest request) {
        // HTTP 요청에서 인증 토큰을 추출합니다.
        String authToken = authTokenApplicationService.resolveAuthToken(request);
        // 추출한 인증 토큰을 사용하여 현재 로그인한 사용자의 이메일을 얻습니다.
        String email = authTokenApplicationService.getUserPk(authToken);
        // 트레이너 애플리케이션 서비스를 사용하여 현재 사용자의 자격증 목록을 조회합니다.
        List<LicenseResponse.List> response = licenseApplicationService.getLicensesByEmail(email);

        return RestResponseUtil.ok("자격증 조회 성공했습니다.", response);
    }

    /**
     * 사용자의 자격증을 등록합니다.
     *
     * @param request           HTTP 요청 객체입니다.
     * @param saveLicenseRequest 자격증 정보를 나타내는 요청 DTO입니다.
     * @param bindingResult     유효성 검사 결과를 저장하는 BindingResult 객체입니다.
     * @return 자격증 등록 결과를 나타내는 ResponseEntity 객체입니다.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/")
    public ResponseEntity<?> saveLicense(HttpServletRequest request, @Valid SaveLicenseRequest saveLicenseRequest, BindingResult bindingResult) {
        // 자격증을 검증하고 결과를 얻어옵니다.
        LicenseResponse.Result licenseResult = licenseOpenApiService.getLicenseVerificationResult(saveLicenseRequest);

        // HTTP 요청에서 인증 토큰을 추출합니다.
        String authToken = authTokenApplicationService.resolveAuthToken(request);

        // 추출한 인증 토큰을 사용하여 현재 로그인한 사용자의 이메일을 얻습니다.
        String email = authTokenApplicationService.getUserPk(authToken);

        // 트레이너 애플리케이션 서비스를 사용하여 자격증을 저장합니다.
        licenseApplicationService.saveLicense(email, licenseResult);

        return RestResponseUtil
                .ok("자격증 등록 성공했습니다.", null);
    }

    /**
     * 사용자의 자격증을 삭제합니다.
     *
     * @param request               HTTP 요청 객체입니다.
     * @param removeLicenseRequest  삭제할 자격증 ID 목록을 나타내는 요청 DTO입니다.
     * @param bindingResult         유효성 검사 결과를 저장하는 BindingResult 객체입니다.
     * @return 자격증 삭제 결과를 나타내는 ResponseEntity 객체입니다.
     */
    @RequestMapping(method = RequestMethod.DELETE, path = "/")
    public ResponseEntity<?> deleteLicense(HttpServletRequest request, @Valid RemoveLicenseRequest removeLicenseRequest, BindingResult bindingResult) {
        // HTTP 요청에서 인증 토큰을 추출합니다.
        String authToken = authTokenApplicationService.resolveAuthToken(request);

        // 추출한 인증 토큰을 사용하여 현재 로그인한 사용자의 이메일을 얻습니다
        String email = authTokenApplicationService.getUserPk(authToken);

        // 삭제할 자격증 ID 목록을 반복하며 각 자격증을 삭제합니다.
        licenseApplicationService.deleteLicenseByEmailAndLicenseId(email, removeLicenseRequest);

        return RestResponseUtil.ok("자격증 삭제 성공했습니다.", null);
    }
}
