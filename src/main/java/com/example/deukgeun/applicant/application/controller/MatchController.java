package com.example.deukgeun.applicant.application.controller;

import com.example.deukgeun.applicant.application.dto.request.SaveMatchInfoRequest;
import com.example.deukgeun.applicant.application.service.ApplicantApplicationService;
import com.example.deukgeun.global.util.RestResponseUtil;
import com.example.deukgeun.job.application.service.JobApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/match")
@RequiredArgsConstructor
public class MatchController {

    private final ApplicantApplicationService applicantApplicationService;
    private final JobApplicationService jobApplicationService;
    @Value("${status.applicant.select}")
    private int APPLICANT_SELECT;
    @Value("${status.applicant.waiting}")
    private int APPLICANT_WAITING;
    @Value("${status.job.inactive}")
    private int JOB_INACTIVE;
    @Value("${status.payment.waiting}")
    private int PAYMENT_WAITING;


    /**
     * 지원자 매칭 정보를 삭제하고 선택 여부를 업데이트하는 요청을 처리합니다.
     *
     * @param id 취소할 지원자의 ID
     * @return 취소 결과를 담은 ResponseEntity
     */
    @RequestMapping(method = RequestMethod.DELETE, path = "/")
    public ResponseEntity<?> cancel(@RequestParam @NotBlank(message = "id is required")Long id) {
        // 주어진 ID로 지원자의 매칭 정보를 삭제합니다.
        applicantApplicationService.deleteMatchInfoById(id);
        // 선택 여부를 APPLICANT_WAITING 으로 업데이트하여 취소 처리합니다.
        applicantApplicationService.updateIsSelectedById(id, APPLICANT_WAITING);

        return RestResponseUtil.ok("취소 성공했습니다.", null);
    }

    /**
     * 지원할 공고 ID를 사용하여 지원가능한 공고인지 확인하는 메서드입니다.
     *
     * @param jobId 확인할 공고 ID
     * @return 지원가능한 공고 확인 여부에 대한 응답 ResponseEntity
     */
    @RequestMapping(method = RequestMethod.GET, path = "/check/{jobId}")
    public ResponseEntity<?> isAnnouncementMatched(@PathVariable Long jobId) {
        applicantApplicationService.isAnnouncementMatchedByJobId(jobId);

        return RestResponseUtil.ok("검사 성공했습니다.", null);
    }

    /**
     * 매칭을 수행하는 메서드입니다.
     *
     * @param saveMatchInfoRequest 매칭 정보를 담고 있는 요청 객체
     * @param bindingResult        요청 객체의 유효성 검사 결과
     * @return 매칭 결과에 대한 응답 ResponseEntity
     */
    @RequestMapping(method = RequestMethod.POST, path = "/")
    public ResponseEntity<?> matching(@Valid SaveMatchInfoRequest saveMatchInfoRequest, BindingResult bindingResult) {
        applicantApplicationService.saveMatchInfo(saveMatchInfoRequest, PAYMENT_WAITING);
        applicantApplicationService.updateIsSelectedById(saveMatchInfoRequest.getApplicantId(), APPLICANT_SELECT);
        jobApplicationService.updateIsActiveByJobId(JOB_INACTIVE, saveMatchInfoRequest.getJobId());

        return RestResponseUtil.ok("매칭 성공했습니다.", null);
    }
}
