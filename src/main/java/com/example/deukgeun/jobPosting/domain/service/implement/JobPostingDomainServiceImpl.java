package com.example.deukgeun.jobPosting.domain.service.implement;

import com.example.deukgeun.jobPosting.application.dto.request.SaveJobPostingRequest;
import com.example.deukgeun.jobPosting.domain.model.aggregate.JobPosting;
import com.example.deukgeun.jobPosting.domain.repository.JobPostingRepository;
import com.example.deukgeun.jobPosting.domain.service.JobPostingDomainService;
import com.example.deukgeun.trainer.domain.model.valueobjcet.Address;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class JobPostingDomainServiceImpl implements JobPostingDomainService {
    private final JobPostingRepository jobPostingRepository;

    @Override
    public boolean existsByIdAndMemberId(Long id, Long memberId) {
        return jobPostingRepository.existsByIdAndMemberId(id, memberId);
    }

    @Override
    public JobPosting findById(Long id) {
        return jobPostingRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("없는 정보 입니다.")
        );
    }

    @Override
    public Page<JobPosting> getListByMemberId(Long memberId, PageRequest pageRequest) {
        return jobPostingRepository.findByMemberId(memberId, pageRequest);
    }

    @Override
    public Page<JobPosting> getListByKeyword(String keyword, PageRequest pageRequest) {
        return jobPostingRepository.findByLikeKeyword(keyword, pageRequest);
    }

    @Override
    public JobPosting save(SaveJobPostingRequest saveJobPostingRequest, Long memberId) {

        JobPosting jobPosting = JobPosting.create(
                memberId,
                saveJobPostingRequest.getTitle(),
                saveJobPostingRequest.getRequirementLicense(),
                saveJobPostingRequest.getRequirementEtc(),
                new Address(
                        saveJobPostingRequest.getPostcode(),
                        saveJobPostingRequest.getJibunAddress(),
                        saveJobPostingRequest.getRoadAddress(),
                        saveJobPostingRequest.getDetailAddress(),
                        saveJobPostingRequest.getExtraAddress()
                ),
                1,
                saveJobPostingRequest.getStartDate(),
                saveJobPostingRequest.getEndDate()
        );

        return jobPostingRepository.save(jobPosting);
    }

    @Override
    public JobPosting updateIsActiveByJobPostingId(int isActive, Long id) {
        JobPosting jobPosting = findById(id);
        jobPosting.updateIsActive(isActive);
        return jobPostingRepository.save(jobPosting);
    }
}
