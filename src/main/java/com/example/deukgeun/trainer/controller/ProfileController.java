package com.example.deukgeun.trainer.controller;

import com.example.deukgeun.commom.util.RestResponseUtil;
import com.example.deukgeun.trainer.entity.Profile;
import com.example.deukgeun.trainer.response.ProfileResponse;
import com.example.deukgeun.trainer.service.implement.ProfileServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/trainer/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileServiceImpl profileService;

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity<?> getDetail(@PathVariable Long id) throws Exception {
        Profile profile = profileService.getProfileByUserId(id);
        ProfileResponse.ProfileAndUserResponse response = new ProfileResponse.ProfileAndUserResponse(profile);

        return RestResponseUtil.okResponse("마이 페이지 조회 성공했습니다.", response);
    }

}
