package com.example.deukgeun.job.application.controller;

import com.example.deukgeun.auth.application.service.implement.AuthTokenApplicationServiceImpl;
import com.example.deukgeun.global.util.RestResponseUtil;
import com.example.deukgeun.job.application.dto.request.SaveApplicantRequest;
import com.example.deukgeun.job.application.dto.response.ApplicantResponse;
import com.example.deukgeun.job.domain.service.ApplicantService;
import com.example.deukgeun.trainer.infrastructure.persistence.TrainerServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController("member.controller.applicant")
@RequestMapping("/api/applicant")
@RequiredArgsConstructor
public class ApplicantController {

    private final ApplicantService applicantService;
    private final AuthTokenApplicationServiceImpl authTokenApplicationService;
    private final TrainerServiceImpl trainerService;

    @RequestMapping(method = RequestMethod.GET, path = "/")
    public ResponseEntity<?> list(Long jobPostingId, int currentPage) {
        Page<ApplicantResponse.ListResponse> list = applicantService.getByJobPostingId(jobPostingId, currentPage);

        return RestResponseUtil.ok("조회 성공했습니다.", list);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/")
    public ResponseEntity<?> save(HttpServletRequest request, @Valid SaveApplicantRequest saveApplicantRequest, BindingResult bindingResult) {
        String authToken = authTokenApplicationService.resolveAuthToken(request);
        Long trainerId = trainerService.getTrainerId(authToken);
        applicantService.save(saveApplicantRequest, trainerId);

        return RestResponseUtil.ok("지원 성공했습니다.", null);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity<?> getApplicantInfo(@PathVariable Long id) {
        ApplicantResponse.ApplicantInfo result = applicantService.getById(id);

        return RestResponseUtil.ok("조회 성공했습니다.", result);
    }
}
