package com.example.deukgeun.job.application.service.implement;

import com.example.deukgeun.job.application.dto.request.SaveJobPostingRequest;
import com.example.deukgeun.job.application.dto.response.JobPostingResponse;
import com.example.deukgeun.job.application.service.JobPostingService;
import com.example.deukgeun.job.domain.entity.JobPosting;
import com.example.deukgeun.job.domain.repository.JobPostingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;

@Service("main.jobPosting.service")
@RequiredArgsConstructor
public class JobPostingServiceImpl implements JobPostingService {
    private final JobPostingRepository jobPostingRepository;

    @Override
    public JobPosting save(SaveJobPostingRequest saveJobPostingRequest, Long memberId) {
        JobPosting jobPosting = JobPosting
                .builder()
                .memberId(memberId)
                .title(saveJobPostingRequest.getTitle())
                .requirementLicense(saveJobPostingRequest.getRequirementLicense())
                .requirementEtc(saveJobPostingRequest.getRequirementEtc())
                .postcode(saveJobPostingRequest.getPostcode())
                .jibunAddress(saveJobPostingRequest.getJibunAddress())
                .detailAddress(saveJobPostingRequest.getDetailAddress())
                .extraAddress(saveJobPostingRequest.getExtraAddress())
                .roadAddress(saveJobPostingRequest.getRoadAddress())
                .startDate(LocalDateTime.parse(saveJobPostingRequest.getStartDate()))
                .endDate(LocalDateTime.parse(saveJobPostingRequest.getEndDate()))
                .isActive(1)
                .build();

        return jobPostingRepository.save(jobPosting);
    }

    @Override
    public JobPosting updateIsActiveByJobPostingId(int isActive, Long jobPostingId) {
        JobPosting foundJobPosting = jobPostingRepository.findById(jobPostingId)
                .orElseThrow(() -> new EntityNotFoundException("찾을수 없는 공고 정보입니다."));
        foundJobPosting.updateIsActive(isActive);
        return jobPostingRepository.save(foundJobPosting);
    }

    @Override
    public Page<JobPostingResponse.ListResponse> getByMemberId(Long memberId, Integer currentPage) {
        PageRequest pageable = PageRequest.of(currentPage, 10);

        return jobPostingRepository.findByMemberId(memberId, pageable);
    }

    @Override
    public Page<JobPostingResponse.ListResponse> getListByKeyword(String keyword, int currentPage) {
        String likeKeyword = "%" + keyword + "%";
        PageRequest pageable = PageRequest.of(currentPage, 10);

        return jobPostingRepository.findByLikeKeyword(likeKeyword, pageable);
    }

    @Override
    public JobPosting getById(Long id) {
        return jobPostingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("없는 공고 정보 입니다."));
    }

    @Override
    public boolean existsByIdAndMemberId(Long id, Long memberId) {
        return jobPostingRepository.existsByIdAndMemberId(id, memberId);
    }
}