package com.example.deukgeun.trainer.application.controller;

import com.example.deukgeun.authToken.application.service.AuthTokenApplicationService;
import com.example.deukgeun.global.util.RestResponseUtil;
import com.example.deukgeun.trainer.application.dto.UpdateProfileRequest;
import com.example.deukgeun.trainer.application.dto.response.ProfileResponse;
import com.example.deukgeun.trainer.application.service.ProfileApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/trainer/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileApplicationService profileApplicationService;
    private final AuthTokenApplicationService authTokenApplicationService;

    /**
     * 특정 트레이너의 프로필 정보를 조회합니다.
     *
     * @param id 조회할 트레이너의 고유 식별자입니다.
     * @return 조회된 트레이너의 프로필 정보를 포함한 응답 객체입니다.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity<?> getProfile(@PathVariable Long id) {
        // 고유 식별자(id)를 사용하여 트레이너 정보를 조회합니다.
        ProfileResponse profileResponse = profileApplicationService.getProfileId(id);

        return RestResponseUtil.ok("트레이너 상세보기 성공했습니다.", profileResponse);
    }

    /**
     * 인증된 사용자의 트레이너 프로필 정보를 조회합니다.
     *
     * @param request HttpServletRequest 객체로, 인증 토큰을 추출하기 위해 사용됩니다.
     * @return 조회된 트레이너 프로필 정보를 포함한 응답 객체입니다.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/")
    public ResponseEntity<?> getProfile(HttpServletRequest request) {
        // 요청에서 인증 토큰을 추출합니다.
        String authToken = authTokenApplicationService.resolveAuthToken(request);

        // 추출한 인증 토큰을 사용하여 사용자의 이메일 정보를 얻습니다
        String email = authTokenApplicationService.getUserPk(authToken);

        // 사용자의 이메일 정보를 기반으로 트레이너 프로필 정보를 조회합니다.
        ProfileResponse profileResponse = profileApplicationService.getProfileByEmail(email);

        return RestResponseUtil.ok("트레이너 상세보기 성공했습니다.", profileResponse);
    }

    /**
     * 현재 로그인한 사용자의 트레이너 프로필 정보를 업데이트합니다.
     *
     * @param request             HttpServletRequest 객체로, 인증 토큰을 추출하기 위해 사용됩니다.
     * @param updateProfileRequest 업데이트할 프로필 정보를 담은 요청 객체입니다.
     * @param bindingResult       데이터 유효성 검사 결과를 담은 BindingResult 객체입니다.
     * @return 업데이트 결과에 대한 응답 객체입니다.
     * @throws Exception 업데이트 과정에서 발생할 수 있는 예외를 처리합니다.
     */
    @RequestMapping(method = RequestMethod.PUT, path = "/")
    public ResponseEntity<?> updateProfile
            (HttpServletRequest request,
            @Valid UpdateProfileRequest updateProfileRequest,
            BindingResult bindingResult) throws Exception {
        // 요청에서 인증 토큰을 추출합니다.
        String authToken = authTokenApplicationService.resolveAuthToken(request);

        // 추출한 인증 토큰을 사용하여 사용자의 이메일 정보를 얻습니다.
        String email = authTokenApplicationService.getUserPk(authToken);

        // 업데이트할 프로필 정보를 트레이너 서비스를 통해 업데이트합니다.
        profileApplicationService.updateProfile(email, updateProfileRequest.getProfile());

        return RestResponseUtil.ok("프로필 정보 수정 성공했습니다.", null);
    }
}
