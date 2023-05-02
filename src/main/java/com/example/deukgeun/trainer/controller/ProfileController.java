package com.example.deukgeun.trainer.controller;

import com.example.deukgeun.commom.service.implement.JwtServiceImpl;
import com.example.deukgeun.commom.service.implement.ValidateServiceImpl;
import com.example.deukgeun.commom.util.RestResponseUtil;
import com.example.deukgeun.trainer.entity.Profile;
import com.example.deukgeun.trainer.request.ProfileUpdateRequest;
import com.example.deukgeun.trainer.response.ProfileResponse;
import com.example.deukgeun.trainer.service.implement.ProfileServiceImpl;
import com.example.deukgeun.trainer.service.implement.UserServiceImpl;
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
    private final UserServiceImpl userService;
    private final ValidateServiceImpl validateService;
    private final JwtServiceImpl jwtService;

    /**
     * profile + user data 가져오기
     * id에 해당되는 data
     *
     * @param id userId
     * @return RestResponseUtil find profile + user data
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity<?> getDetailByUserId(@PathVariable Long id) throws Exception {
        Profile profile = profileService.getProfileByUserId(id);
        ProfileResponse.ProfileAndUserResponse response = new ProfileResponse.ProfileAndUserResponse(profile);

        return RestResponseUtil.okResponse("트레이너 상세보기 성공했습니다.", response);
    }

    /**
     * profile + user data 가져오기
     * authToken 에서 추출한 user data 로 userId를 구해 데이터 비교
     *
     * @param request authToken 추출을 위한 파라미터
     * @return  RestResponseUtil find profile + user data
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET, path = "/")
    public ResponseEntity<?> getDetailByAuthToke(HttpServletRequest request) throws Exception {
        String authToken = jwtService.resolveAuthToken(request);
        Long userId = userService.getUserId(authToken);
        Profile profile = profileService.getProfileByUserId(userId);
        ProfileResponse.ProfileAndUserResponse response = new ProfileResponse.ProfileAndUserResponse(profile);

        return RestResponseUtil
                .okResponse("트레이너 상세보기 성공했습니다.", response);
    }

    /**
     * 프로필 이미지 수정
     * DB 데이터 수정, 서버에 파일 저장, 서버에 저장되있던 파일 삭제
     *
     * @param request authToken 추출을 위한 파라미터
     * @param updateRequest prfile
     * @param bindingResult form data validator 결과를 담는 역할
     * @return RestResponseUtil
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.PUT, path = "/")
    public ResponseEntity<?> update(HttpServletRequest request, @Valid ProfileUpdateRequest updateRequest, BindingResult bindingResult) throws Exception {
        validateService.errorMessageHandling(bindingResult);
        String authToken = jwtService.resolveAuthToken(request);
        profileService.updateProfile(updateRequest.getProfile(), authToken);

        return RestResponseUtil.okResponse("내 정보 수정 성공했습니다.", null);
    }
}
