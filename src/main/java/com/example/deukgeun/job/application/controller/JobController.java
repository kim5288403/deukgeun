package com.example.deukgeun.job.application.controller;

import com.example.deukgeun.authToken.application.service.AuthTokenApplicationService;
import com.example.deukgeun.global.util.RestResponseUtil;
import com.example.deukgeun.job.application.dto.request.SaveJobRequest;
import com.example.deukgeun.job.application.dto.response.JobResponse;
import com.example.deukgeun.job.application.service.JobApplicationService;
import com.example.deukgeun.job.domain.model.aggregate.Job;
import com.example.deukgeun.member.application.service.MemberApplicationService;
import com.example.deukgeun.member.domain.aggregate.Member;
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

@RestController
@RequestMapping("/api/job")
@RequiredArgsConstructor
public class JobController {

    private final JobApplicationService jobApplicationService;
    private final MemberApplicationService memberApplicationService;
    private final AuthTokenApplicationService authTokenApplicationService;

    /**
     * 공고 소유권을 확인하는 GET 요청을 처리합니다.
     *
     * @param request HTTP 요청 객체입니다.
     * @param id      공고 식별자입니다.
     * @return 공고 소유권 확인 결과를 담은 응답입니다.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/check")
    public ResponseEntity<?> checkJobOwnership(HttpServletRequest request, Long id) {
        String authToken = authTokenApplicationService.resolveAuthToken(request);
        String email = authTokenApplicationService.getUserPk(authToken);
        Member member = memberApplicationService.findByEmail(email);
        boolean result = jobApplicationService.existsByIdAndMemberId(id, member.getId());

        return RestResponseUtil.ok("체크 성공했습니다.", result);
    }

    /**
     * 공고의 세부 정보를 조회하는 GET 요청을 처리합니다.
     *
     * @param id 공고의 식별자입니다.
     * @return 공고의 세부 정보를 담은 응답입니다.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity<?> detail(@PathVariable Long id) {
        Job detail = jobApplicationService.findById(id);

        return RestResponseUtil.ok("조회 성공했습니다.", detail);
    }

    /**
     * 키워드를 기반으로 공고 목록을 조회하는 GET 요청을 처리합니다.
     *
     * @param keyword      검색 키워드입니다.
     * @param currentPage  현재 페이지 번호입니다.
     * @return 키워드를 기반으로 조회된 공고 목록을 담은 응답입니다.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/")
    public ResponseEntity<?> getListByKeyword(String keyword, int currentPage) {
        Page<JobResponse.List> list = jobApplicationService.getListByKeyword(keyword, currentPage);

        return RestResponseUtil.ok("조회 성공했습니다.", list);
    }

    /**
     * 사용자 식별자를 기반으로 해당 사용자의 공고 목록을 조회하는 GET 요청을 처리합니다.
     *
     * @param request     HTTP 요청 객체입니다.
     * @param currentPage 현재 페이지 번호입니다.
     * @return 사용자의 공고 목록을 담은 응답입니다.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/member")
    public ResponseEntity<?> getListByMemberId(HttpServletRequest request, int currentPage) {
        String token = authTokenApplicationService.resolveAuthToken(request);
        String userPk = authTokenApplicationService.getUserPk(token);
        Member member = memberApplicationService.findByEmail(userPk);
        Page<JobResponse.List> list = jobApplicationService.getListByMemberId(member.getId(), currentPage);

        return RestResponseUtil.ok("조회 성공했습니다.", list);
    }

    /**
     * 공고를 등록하는 POST 요청을 처리합니다.
     *
     * @param request           HTTP 요청 객체입니다.
     * @param saveJobRequest    공고 등록 요청 DTO 입니다.
     * @param bindingResult     요청 데이터 유효성 검사 결과입니다.
     * @return 공고 등록 결과를 담은 응답입니다.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/")
    public ResponseEntity<?> save(HttpServletRequest request, @Valid SaveJobRequest saveJobRequest, BindingResult bindingResult) {
        String token = authTokenApplicationService.resolveAuthToken(request);
        String userPk = authTokenApplicationService.getUserPk(token);
        Member member = memberApplicationService.findByEmail(userPk);
        jobApplicationService.save(saveJobRequest, member.getId());

        return RestResponseUtil.ok("등록 성공했습니다.", null);
    }
}
