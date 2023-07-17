package com.example.deukgeun.job.application.controller;

import com.example.deukgeun.global.util.RestResponseUtil;
import com.example.deukgeun.job.domain.service.JobPostingService;
import com.example.deukgeun.job.application.dto.request.SaveMatchInfoRequest;
import com.example.deukgeun.job.domain.service.ApplicantService;
import com.example.deukgeun.job.domain.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController("member.controller.matchInfo")
@RequestMapping("/api/match")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;
    private final JobPostingService jobPostingService;
    private final ApplicantService applicantService;

    @RequestMapping(method = RequestMethod.POST, path = "/")
    public ResponseEntity<?> select(@Valid SaveMatchInfoRequest saveMatchInfoRequest, BindingResult bindingResult) {
        matchService.save(saveMatchInfoRequest);
        applicantService.updateIsSelectedByApplicantId(saveMatchInfoRequest.getApplicantId(), 1);
        jobPostingService.updateIsActiveByJobPostingId(2, saveMatchInfoRequest.getJobPostingId());

        return RestResponseUtil.ok("매칭 성공했습니다.", null);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/check/{id}")
    public ResponseEntity<?> isAnnouncementMatched(@PathVariable Long id) {
        matchService.isAnnouncementMatchedByJobPostingId(id);

        return RestResponseUtil.ok("검사 성공했습니다.", null);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/")
    public ResponseEntity<?> cancel(@RequestParam @NotBlank(message = "applicantId is required")Long applicantId) {
        matchService.deleteByApplicantId(applicantId);
        applicantService.updateIsSelectedByApplicantId(applicantId, 0);

        return RestResponseUtil.ok("취소 성공했습니다.", null);
    }



}
