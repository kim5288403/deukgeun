package com.example.deukgeun.applicant.application.controller;

import com.example.deukgeun.applicant.application.dto.response.ApplicantResponse;
import com.example.deukgeun.applicant.application.service.ApplicantApplicationService;
import com.example.deukgeun.applicant.domain.model.aggregate.Applicant;
import com.example.deukgeun.authToken.application.service.AuthTokenApplicationService;
import com.example.deukgeun.global.util.RestResponseUtil;
import com.example.deukgeun.applicant.application.dto.request.SaveApplicantRequest;
import com.example.deukgeun.trainer.application.service.TrainerApplicationService;
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

    private final ApplicantApplicationService applicantApplicationService;
    private final AuthTokenApplicationService authTokenApplicationService;
    private final TrainerApplicationService trainerApplicationService;

    @RequestMapping(method = RequestMethod.GET, path = "/")
    public ResponseEntity<?> list(Long jobPostingId, int currentPage) {
        Page<ApplicantResponse.ListResponse> list = applicantApplicationService.getByJobPostingId(jobPostingId, currentPage);

        return RestResponseUtil.ok("조회 성공했습니다.", list);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/")
    public ResponseEntity<?> save(HttpServletRequest request, @Valid SaveApplicantRequest saveApplicantRequest, BindingResult bindingResult) {
        String authToken = authTokenApplicationService.resolveAuthToken(request);
        String email = authTokenApplicationService.getUserPk(authToken);
        Long trainerId = trainerApplicationService.findByEmail(email).getId();

        applicantApplicationService.save(saveApplicantRequest, trainerId);

        return RestResponseUtil.ok("지원 성공했습니다.", null);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity<?> getApplicantInfo(@PathVariable Long id) {
        Applicant applicant = applicantApplicationService.findById(id);
        ApplicantResponse.ApplicantInfo result = new ApplicantResponse.ApplicantInfo(applicant);

        return RestResponseUtil.ok("조회 성공했습니다.", result);
    }
}
