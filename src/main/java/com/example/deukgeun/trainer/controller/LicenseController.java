package com.example.deukgeun.trainer.controller;

import com.example.deukgeun.commom.service.implement.ValidateServiceImpl;
import com.example.deukgeun.commom.util.RestResponseUtil;
import com.example.deukgeun.trainer.request.SaveLicenseRequest;
import com.example.deukgeun.trainer.response.LicenseListResponse;
import com.example.deukgeun.trainer.service.implement.LicenseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/trainer/license")
@RequiredArgsConstructor
public class LicenseController {
    private final LicenseServiceImpl licenseService;
    private final ValidateServiceImpl validateService;

    /**
     * userId 에 해당하는 자격증 데이터 가져오기
     *
     * @param id userId
     * @return RestResponseUtil
     */
    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity<?> getListById(@PathVariable Long id) {
        List<LicenseListResponse> response = licenseService.findByUserId(id);

        return RestResponseUtil
                .okResponse("자격증 조회 성공했습니다.", response);
    }

    /**
     * authToken 에 해당하는 자격증 데이터 가져오기
     *
     * @param request authToken 추출을 위한 파라미터
     * @return RestResponseUtil 자격증 데이터
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET, path = "/")
    public ResponseEntity<?> getListByAuthToken(HttpServletRequest request) throws Exception {
        String authToken = request.getHeader("Authorization").replace("Bearer ", "");
        List<LicenseListResponse> response = licenseService.findByEmail(authToken);

        return RestResponseUtil
                .okResponse("자격증 조회 성공했습니다.", response);
    }


    @RequestMapping(method = RequestMethod.POST, path = "/")
    public ResponseEntity<?> saveLicense(HttpServletRequest request, @Valid SaveLicenseRequest saveLicenseRequest, BindingResult bindingResult) throws Exception {
        String authToken = request.getHeader("Authorization").replace("Bearer ", "");
        validateService.errorMessageHandling(bindingResult);
//        licenseService.saveLicense(saveLicenseRequest, authToken);
        WebClient webClient = WebClient.builder().build();
        return RestResponseUtil
                .okResponse("자격증 등록 성공했습니다.", null);
    }


}
