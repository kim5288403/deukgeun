package com.example.deukgeun.trainer.application.controller;

import com.example.deukgeun.authToken.application.service.AuthTokenApplicationService;
import com.example.deukgeun.global.util.RestResponseUtil;
import com.example.deukgeun.trainer.application.dto.request.UpdateProfileRequest;
import com.example.deukgeun.trainer.application.dto.response.ProfileResponse;
import com.example.deukgeun.trainer.application.service.ProfileApplicationService;
import com.example.deukgeun.trainer.application.service.TrainerApplicationService;
import com.example.deukgeun.trainer.domain.model.entity.Profile;
import com.example.deukgeun.trainer.domain.model.entity.Trainer;
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
    private final TrainerApplicationService trainerService;
    private final AuthTokenApplicationService authTokenApplicationService;

    /**
     * 특정 사용자 ID에 해당하는 프로필 상세 정보를 조회합니다.
     *
     * @param id 조회할 사용자의 ID
     * @return ResponseEntity 객체
     */
    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity<?> getByUserId(@PathVariable Long id) {
        // profileService 를 통해 해당 사용자 ID에 해당하는 프로필 정보를 조회합니다.
        Profile profile = profileApplicationService.findByTrainerId(id);

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
        String authToken = authTokenApplicationService.resolveAuthToken(request);
        String email = authTokenApplicationService.getUserPk(authToken);
        Long trainerId = trainerService.findByEmail(email).getId();

        // 사용자 ID에 해당하는 프로필 정보를 조회합니다.
        Profile profile = profileApplicationService.findByTrainerId(trainerId);

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
        String authToken = authTokenApplicationService.resolveAuthToken(request);
        String email = authTokenApplicationService.getUserPk(authToken);
        Trainer trainer = trainerService.findByEmail(email);


        // 프로필 서비스를 이용하여 사용자의 프로필 정보를 업데이트합니다.
        Long profileId = profileApplicationService.findByTrainerId(trainer.getId()).getId();
        profileApplicationService.updateProfile(updateRequest.getProfile(), profileId);

        return RestResponseUtil.ok("프로필 정보 수정 성공했습니다.", null);
    }
}
