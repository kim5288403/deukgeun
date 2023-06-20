package com.example.deukgeun.trainer.controller;

import com.example.deukgeun.commom.service.implement.JwtServiceImpl;
import com.example.deukgeun.commom.util.RestResponseUtil;
import com.example.deukgeun.trainer.entity.Profile;
import com.example.deukgeun.trainer.request.UpdateProfileRequest;
import com.example.deukgeun.trainer.response.ProfileResponse;
import com.example.deukgeun.trainer.service.implement.MemberServiceImpl;
import com.example.deukgeun.trainer.service.implement.ProfileServiceImpl;
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
    private final ProfileServiceImpl profileService;
    private final MemberServiceImpl memberService;
    private final JwtServiceImpl jwtService;

    /**
     * 특정 사용자 ID에 해당하는 프로필 상세 정보를 조회합니다.
     *
     * @param id 조회할 사용자의 ID
     * @return ResponseEntity 객체
     */
    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity<?> getByUserId(@PathVariable Long id) {
        // profileService 를 통해 해당 사용자 ID에 해당하는 프로필 정보를 조회합니다.
        Profile profile = profileService.getByUserId(id);

        // 조회된 프로필 정보를 이용하여 ProfileResponse 객체를 생성합니다.
        ProfileResponse.ProfileAndUserResponse response = new ProfileResponse.ProfileAndUserResponse(profile);

        return RestResponseUtil.ok("트레이너 상세보기 성공했습니다.", response);
    }

    /**
     * 현재 인증된 사용자의 프로필 상세 정보를 조회합니다.
     *
     * @param request HttpServletRequest 객체
     * @return ResponseEntity 객체
     */
    @RequestMapping(method = RequestMethod.GET, path = "/")
    public ResponseEntity<?> getByAuthToken(HttpServletRequest request) {
        // 요청 헤더에서 인증 토큰을 추출합니다.
        String authToken = jwtService.resolveAuthToken(request);

        // 인증 토큰을 이용하여 사용자 ID를 조회합니다.
        Long userId = memberService.getUserId(authToken);

        // 사용자 ID에 해당하는 프로필 정보를 조회합니다.
        Profile profile = profileService.getByUserId(userId);

        // 조회된 프로필 정보를 이용하여 ProfileResponse 객체를 생성합니다.
        ProfileResponse.ProfileAndUserResponse response = new ProfileResponse.ProfileAndUserResponse(profile);

        return RestResponseUtil.ok("트레이너 상세보기 성공했습니다.", response);
    }

    /**
     * 사용자의 프로필 정보를 수정합니다.
     *
     * @param request        HttpServletRequest 객체
     * @param updateRequest  UpdateProfileRequest 객체
     * @param bindingResult  BindingResult 객체
     * @return ResponseEntity 객체
     * @throws Exception 예외 발생 시
     */
    @RequestMapping(method = RequestMethod.POST, path = "/")
    public ResponseEntity<?> update(HttpServletRequest request, @Valid UpdateProfileRequest updateRequest, BindingResult bindingResult) throws Exception {
        // 요청 헤더에서 인증 토큰을 추출합니다.
        String authToken = jwtService.resolveAuthToken(request);

        // 프로필 서비스를 이용하여 사용자의 프로필 정보를 업데이트합니다.
        profileService.updateProfile(updateRequest.getProfile(), authToken);

        return RestResponseUtil.ok("프로필 정보 수정 성공했습니다.", null);
    }
}
