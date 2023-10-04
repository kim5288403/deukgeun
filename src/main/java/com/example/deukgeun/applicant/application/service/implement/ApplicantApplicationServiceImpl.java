package com.example.deukgeun.applicant.application.service.implement;

import com.example.deukgeun.applicant.application.dto.request.SaveApplicantRequest;
import com.example.deukgeun.applicant.application.dto.response.ApplicantResponse;
import com.example.deukgeun.applicant.application.service.ApplicantApplicationService;
import com.example.deukgeun.applicant.domain.dto.SaveApplicantDTO;
import com.example.deukgeun.applicant.domain.model.aggregate.Applicant;
import com.example.deukgeun.applicant.domain.service.ApplicantDomainService;
import com.example.deukgeun.applicant.infrastructure.persistence.mapper.ApplicantMapper;
import com.example.deukgeun.job.domain.model.aggregate.Job;
import com.example.deukgeun.member.domain.aggregate.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Period;

@Service
@RequiredArgsConstructor
public class ApplicantApplicationServiceImpl implements ApplicantApplicationService {
    private final ApplicantDomainService applicantDomainService;
    private final ApplicantMapper applicantMapper;

    /**
     * 지정된 ID를 사용하여 지원자 정보를 조회하는 메서드입니다.
     *
     * @param id 조회할 지원자의 고유 ID
     * @return 조회된 지원자 정보를 포함하는 Applicant 객체
     */
    @Override
    public Applicant findById(Long id) {
        return applicantDomainService.findById(id);
    }

    /**
     * 공고 ID를 사용하여 페이지네이션된 지원자 목록을 조회하는 메서드입니다.
     *
     * @param jobId 지원자를 조회할 공고 ID
     * @param currentPage 현재 페이지 번호
     * @return 페이지네이션된 지원자 목록을 포함하는 Page 객체
     */
    @Override
    public Page<ApplicantResponse.List> getListByJobId(Long jobId, int currentPage) {
        PageRequest pageRequest = PageRequest.of(currentPage, 10);

        return applicantDomainService.findPageByJobId(jobId, pageRequest)
                .map(applicantMapper::toApplicantResponseList);
    }

    /**
     * 특정 공고 및 작성자에 대한 지원자 정보를 가져옵니다.
     *
     * @param applicant 지원 정보.
     * @param member 공고 작성자 정보.
     * @param job 공고 정보.
     * @return 지원자 정보를 담은 ApplicantResponse.Info 객체.
     */
    @Override
    public ApplicantResponse.Info getApplicantInfo(Applicant applicant, Member member, Job job) {
        LocalDateTime startDate = job.getStartDate();
        LocalDateTime endDate = job.getEndDate();

        Period period = Period.between(startDate.toLocalDate(), endDate.toLocalDate());

        return applicantMapper.toApplicantResponseInfo(applicant, member, job, period.getDays());
    }

    /**
     * SaveApplicantRequest 트레이너 ID를 사용하여 지원자 정보를 저장하는 메서드입니다.
     *
     * @param saveApplicantRequest 저장할 지원자 정보를 담은 요청 객체
     * @param trainerId 트레이너의 고유 ID
     * @return 저장된 지원자 정보를 포함하는 Applicant 객체
     */
    @Override
    public Applicant save(SaveApplicantRequest saveApplicantRequest, Long trainerId) {
        applicantDomainService.existsByJobIdAndTrainerId(saveApplicantRequest.getJobId(), trainerId);

        SaveApplicantDTO saveApplicantDTO = applicantMapper.toSaveApplicantDto(trainerId, saveApplicantRequest);

        return applicantDomainService.save(saveApplicantDTO);
    }

    /**
     * 고유 지원자 ID와 선택 여부(isSelected) 정보를 사용하여 지원자 정보를 업데이트하는 메서드입니다.
     *
     * @param id 업데이트할 지원자의 고유 ID
     * @param isSelected 새로운 선택 여부 값을 나타내는 정수 값
     */
    @Override
    public void updateIsSelectedById(Long id, int isSelected) {
        applicantDomainService.updateIsSelectedById(id, isSelected);
    }
}
