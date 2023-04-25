package com.example.deukgeun.trainer.controller;


import com.example.deukgeun.commom.service.implement.ValidateServiceImpl;
import com.example.deukgeun.commom.util.RestResponseUtil;
import com.example.deukgeun.trainer.request.SaveProfileRequest;
import com.example.deukgeun.trainer.service.implement.ProfileServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/trainer/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileServiceImpl profileService;
    private final ValidateServiceImpl validateService;

    @RequestMapping(method = RequestMethod.POST, path = "/")
    public ResponseEntity<?> save(@Valid SaveProfileRequest request, BindingResult bindingResult) {
        validateService.errorMessageHandling(bindingResult);
        profileService.save(request);

        return RestResponseUtil.okResponse("프로필 이미지 저장 성공 했습니다.", null);
    }

}
