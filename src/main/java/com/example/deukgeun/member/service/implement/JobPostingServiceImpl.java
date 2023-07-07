package com.example.deukgeun.member.service.implement;

import com.example.deukgeun.global.entity.JobPosting;
import com.example.deukgeun.global.repository.JobPostingRepository;
import com.example.deukgeun.member.request.SaveJobPostingRequest;
import com.example.deukgeun.member.service.JobPostingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service("member.jobPosting.service")
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
}
