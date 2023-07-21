package com.example.deukgeun.trainer.application.controller;

import com.example.deukgeun.authToken.application.service.implement.AuthTokenApplicationServiceImpl;
import com.example.deukgeun.global.util.RestResponseUtil;
import com.example.deukgeun.trainer.application.dto.request.RemoveLicenseRequest;
import com.example.deukgeun.trainer.application.dto.request.SaveLicenseRequest;
import com.example.deukgeun.trainer.application.dto.response.LicenseListResponse;
import com.example.deukgeun.trainer.application.dto.response.LicenseResultResponse;
import com.example.deukgeun.trainer.application.service.implement.LicenseServiceImpl;
import com.example.deukgeun.trainer.application.service.implement.TrainerApplicationServiceImpl;
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
    private final LicenseServiceImpl licenseService;
    private final TrainerApplicationServiceImpl trainerService;
    private final AuthTokenApplicationServiceImpl authTokenApplicationService;

    /**
     * ID를 기반으로 자격증 목록을 조회합니다.
     *
     * @param id 조회할 자격증의 사용자 ID
     * @return ResponseEntity 객체
     */
    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity<?> getListById(@PathVariable Long id) {
        List<LicenseListResponse> response = licenseService.findByTrainerId(id);

        return RestResponseUtil
                .ok("자격증 조회 성공했습니다.", response);
    }

    /**
     * 인증 토큰을 사용하여 자격증 목록을 조회합니다.
     *
     * @param request HttpServletRequest 객체
     * @return ResponseEntity 객체
     */
    @RequestMapping(method = RequestMethod.GET, path = "/")
    public ResponseEntity<?> getListByAuthToken(HttpServletRequest request) {
        // 인증 토큰을 사용하여 사용자 ID 조회
        String authToken = authTokenApplicationService.resolveAuthToken(request);
        String email = authTokenApplicationService.getUserPk(authToken);
        Long trainerId = trainerService.findByEmail(email).getId();

        // 사용자 ID를 기반으로 자격증 목록 조회
        List<LicenseListResponse> response = licenseService.findByTrainerId(trainerId);

        return RestResponseUtil
                .ok("자격증 조회 성공했습니다.", response);
    }

    /**
     * 자격증을 저장합니다.
     *
     * @param request             HttpServletRequest 객체
     * @param saveLicenseRequest  저장할 자격증 요청 객체
     * @param bindingResult       유효성 검사 결과 객체
     * @return ResponseEntity 객체
     * @throws Exception 예외 발생 시
     */
    @RequestMapping(method = RequestMethod.POST, path = "/")
    public ResponseEntity<?> save(HttpServletRequest request, @Valid SaveLicenseRequest saveLicenseRequest, BindingResult bindingResult) throws Exception {
        // 자격증 진위여부를 위한 외부 API 호출
        LicenseResultResponse licenseResult = licenseService.getLicenseVerificationResult(saveLicenseRequest);
        licenseService.checkLicense(licenseResult);

        // 인증 토큰에서 사용자 ID 추출
        String authToken = authTokenApplicationService.resolveAuthToken(request);
        String email = authTokenApplicationService.getUserPk(authToken);
        Long trainerId = trainerService.findByEmail(email).getId();

        // 자격증 저장
        licenseResult.setNo(saveLicenseRequest.getNo());
        licenseService.save(licenseResult, trainerId);

        return RestResponseUtil
                .ok("자격증 등록 성공했습니다.", null);
    }

    /**
     * 자격증을 삭제합니다.
     *
     * @param request        삭제할 자격증 요청 객체
     * @param bindingResult  유효성 검사 결과 객체
     * @return ResponseEntity 객체
     */
    @RequestMapping(method = RequestMethod.DELETE, path = "/")
    public ResponseEntity<?> delete(@Valid RemoveLicenseRequest request, BindingResult bindingResult) {
        // 각 자격증 ID를 기반으로 자격증 삭제
        request.getIds().forEach(licenseService::delete);

        return RestResponseUtil
                .ok("자격증 삭제 성공했습니다.", null);
    }
}
