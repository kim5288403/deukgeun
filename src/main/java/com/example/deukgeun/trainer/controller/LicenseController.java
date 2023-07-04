package com.example.deukgeun.trainer.controller;

import com.example.deukgeun.commom.service.implement.TokenServiceImpl;
import com.example.deukgeun.commom.util.RestResponseUtil;
import com.example.deukgeun.trainer.request.RemoveLicenseRequest;
import com.example.deukgeun.trainer.request.SaveLicenseRequest;
import com.example.deukgeun.trainer.response.LicenseListResponse;
import com.example.deukgeun.trainer.response.LicenseResultResponse;
import com.example.deukgeun.trainer.service.implement.LicenseServiceImpl;
import com.example.deukgeun.trainer.service.implement.TrainerServiceImpl;
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
    private final TrainerServiceImpl trainerService;
    private final TokenServiceImpl tokenService;

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
        String authToken = tokenService.resolveAuthToken(request);
        Long trainerId = trainerService.getTrainerId(authToken);

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
        String authToken = tokenService.resolveAuthToken(request);
        Long trainerid = trainerService.getTrainerId(authToken);

        // 자격증 저장
        licenseResult.setNo(saveLicenseRequest.getNo());
        licenseService.save(licenseResult, trainerid);

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
