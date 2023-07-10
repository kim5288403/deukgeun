package com.example.deukgeun.trainer.controller;

import com.example.deukgeun.global.util.RestResponseUtil;
import com.example.deukgeun.main.service.implement.TokenServiceImpl;
import com.example.deukgeun.trainer.request.SaveApplicantRequest;
import com.example.deukgeun.trainer.service.ApplicantService;
import com.example.deukgeun.trainer.service.implement.TrainerServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController("trainer.applicant.controller")
@RequestMapping("/api/trainer/applicant")
@RequiredArgsConstructor
public class ApplicantController {

    private final ApplicantService applicantService;
    private final TokenServiceImpl tokenService;
    private final TrainerServiceImpl trainerService;

    @RequestMapping(method = RequestMethod.POST, path = "/")
    public ResponseEntity<?> save(HttpServletRequest request, @Valid SaveApplicantRequest saveApplicantRequest, BindingResult bindingResult) {
        String authToken = tokenService.resolveAuthToken(request);
        Long trainerId = trainerService.getTrainerId(authToken);
        applicantService.save(saveApplicantRequest, trainerId);

        return RestResponseUtil.ok("지원 성공했습니다.", null);
    }
}
