package com.example.deukgeun.applicant.application.controller;

import com.example.deukgeun.applicant.application.dto.request.SaveApplicantRequest;
import com.example.deukgeun.applicant.application.dto.response.ApplicantResponse;
import com.example.deukgeun.applicant.application.service.ApplicantApplicationService;
import com.example.deukgeun.applicant.domain.model.aggregate.Applicant;
import com.example.deukgeun.authToken.application.service.AuthTokenApplicationService;
import com.example.deukgeun.global.util.RestResponseUtil;
import com.example.deukgeun.job.application.service.JobApplicationService;
import com.example.deukgeun.job.domain.model.aggregate.Job;
import com.example.deukgeun.member.application.service.MemberApplicationService;
import com.example.deukgeun.member.domain.entity.Member;
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
    private final MemberApplicationService memberApplicationService;
    private final JobApplicationService jobApplicationService;

    /**
     * 지정된 채용 공고 ID와 현재 페이지 번호를 기반으로 지원자 목록을 조회합니다.
     *
     * @param jobId 채용 공고의 ID
     * @param currentPage 현재 페이지 번호
     * @return 지원자 목록 조회 결과를 담은 ResponseEntity
     */
    @RequestMapping(method = RequestMethod.GET, path = "/")
    public ResponseEntity<?> list(Long jobId, int currentPage) {
        // 지정된 ID와 페이지 번호로 지원자 목록을 가져옵니다.
        Page<ApplicantResponse.List> list = applicantApplicationService.getByJobId(jobId, currentPage);

        return RestResponseUtil.ok("조회 성공했습니다.", list);
    }

    /**
     * 지원자 정보를 저장하는 요청을 처리합니다.
     *
     * @param request HTTP 요청 객체
     * @param saveApplicantRequest 저장할 지원자 정보
     * @param bindingResult 데이터 유효성 검증 결과
     * @return 지원자 정보 저장 결과를 담은 ResponseEntity
     */
    @RequestMapping(method = RequestMethod.POST, path = "/")
    public ResponseEntity<?> save(HttpServletRequest request, @Valid SaveApplicantRequest saveApplicantRequest, BindingResult bindingResult) {
        // 요청에서 인증 토큰을 가져옵니다.
        String authToken = authTokenApplicationService.resolveAuthToken(request);
        // 토큰으로부터 사용자 이메일을 추출합니다.
        String email = authTokenApplicationService.getUserPk(authToken);
        // 이메일을 사용하여 트레이너 ID를 조회합니다.
        Long trainerId = trainerApplicationService.findByEmail(email).getId();
        // 지원자 정보를 저장합니다.
        applicantApplicationService.save(saveApplicantRequest, trainerId);

        return RestResponseUtil.ok("지원 성공했습니다.", null);
    }

    /**
     * 지원자 정보를 조회하는 요청을 처리합니다.
     *
     * @param id 조회할 지원자의 ID
     * @return 지원자 정보 조회 결과를 담은 ResponseEntity
     */
    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity<?> getApplicantInfo(@PathVariable Long id) {
        // 주어진 ID로 지원자 정보를 조회합니다
        Applicant applicant = applicantApplicationService.findById(id);
        // 지원한 채용 공고 정보를 조회합니다.
        Job job = jobApplicationService.findById(applicant.getJobId());
        // 지원한 채용 공고의 멤버 정보를 조회합니다.
        Member member = memberApplicationService.findById(job.getMemberId());
        // 조회 결과를 ApplicantResponse.ApplicantInfo 객체로 만듭니다
        ApplicantResponse.ApplicantInfo result = new ApplicantResponse.ApplicantInfo(applicant, member, job);

        return RestResponseUtil.ok("조회 성공했습니다.", result);
    }
}
