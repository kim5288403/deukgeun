package com.example.deukgeun.trainer.application.controller;

import com.example.deukgeun.authToken.application.service.AuthTokenApplicationService;
import com.example.deukgeun.global.util.RestResponseUtil;
import com.example.deukgeun.trainer.application.dto.UpdateProfileRequest;
import com.example.deukgeun.trainer.application.dto.response.ProfileResponse;
import com.example.deukgeun.trainer.application.service.TrainerApplicationService;
import com.example.deukgeun.trainer.domain.model.aggregate.Trainer;
import com.example.deukgeun.trainer.domain.model.entity.Profile;
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
    private final TrainerApplicationService trainerApplicationService;
    private final AuthTokenApplicationService authTokenApplicationService;

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity<?> getProfileByTrainerId(@PathVariable Long id) {
        Trainer trainer = trainerApplicationService.findById(id);
        Profile profile = trainer.getProfile();

        return RestResponseUtil.ok("트레이너 상세보기 성공했습니다.", profile);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/")
    public ResponseEntity<?> getProfileByAuthToken(HttpServletRequest request) {
        String authToken = authTokenApplicationService.resolveAuthToken(request);
        String email = authTokenApplicationService.getUserPk(authToken);
        ProfileResponse profileResponse = trainerApplicationService.getProfile(email);

        return RestResponseUtil.ok("트레이너 상세보기 성공했습니다.", profileResponse);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/")
    public ResponseEntity<?> updateProfile(HttpServletRequest request, @Valid UpdateProfileRequest updateProfileRequest, BindingResult bindingResult) throws Exception {
        String authToken = authTokenApplicationService.resolveAuthToken(request);
        String email = authTokenApplicationService.getUserPk(authToken);
        trainerApplicationService.updateProfile(email, updateProfileRequest.getProfile());

        return RestResponseUtil.ok("프로필 정보 수정 성공했습니다.", null);
    }
}
