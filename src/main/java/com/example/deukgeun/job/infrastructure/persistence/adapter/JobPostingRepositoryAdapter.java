package com.example.deukgeun.job.infrastructure.persistence.adapter;

import com.example.deukgeun.job.domain.model.aggregate.JobPosting;
import com.example.deukgeun.job.domain.repository.JobPostingRepository;
import com.example.deukgeun.job.infrastructure.persistence.model.entity.JobPostingEntity;
import com.example.deukgeun.job.infrastructure.persistence.model.valueobject.JobAddressVo;
import com.example.deukgeun.job.infrastructure.persistence.repository.JobPostingJpaRepository;
import com.example.deukgeun.member.domain.entity.Member;
import com.example.deukgeun.member.infrastructure.persistence.entity.MemberEntity;
import com.example.deukgeun.trainer.domain.model.valueobjcet.Address;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JobPostingRepositoryAdapter implements JobPostingRepository {
    private final JobPostingJpaRepository jobPostingJpaRepository;

    @Override
    public boolean existsByIdAndMemberId(Long id, Long memberId) {
        return jobPostingJpaRepository.existsByIdAndMemberId(id, memberId);
    }

    @Override
    public Optional<JobPosting> findById(Long id) {
        Optional<JobPostingEntity> jobPostingEntity = jobPostingJpaRepository.findById(id);
        return jobPostingEntity.map(this::convert);
    }

    @Override
    public Page<JobPosting> findByMemberId(Long memberId, PageRequest pageRequest) {
        Page<JobPostingEntity> jobPostingEntities = jobPostingJpaRepository.findByMemberId(memberId, pageRequest);
        return jobPostingEntities.map(this::convert);
    }

    @Override
    public Page<JobPosting> findByLikeKeyword(String keyword, PageRequest pageRequest) {
        Page<JobPostingEntity> jobPostingEntities = jobPostingJpaRepository.findByLikeKeyword(keyword, pageRequest);
        return jobPostingEntities.map(this::convert);
    }

    @Override
    public JobPosting save(JobPosting jobPosting) {
        JobPostingEntity jobPostingEntity = jobPostingJpaRepository.save(convert(jobPosting));
        return convert(jobPostingEntity);
    }

    private JobPosting convert(JobPostingEntity jobPostingEntity) {
        return new JobPosting(
                jobPostingEntity.getId(),
                jobPostingEntity.getMemberId(),
                jobPostingEntity.getTitle(),
                jobPostingEntity.getRequirementLicense(),
                jobPostingEntity.getRequirementEtc(),
                new Address(
                        jobPostingEntity.getJobAddressVo().getPostcode(),
                        jobPostingEntity.getJobAddressVo().getJibunAddress(),
                        jobPostingEntity.getJobAddressVo().getRoadAddress(),
                        jobPostingEntity.getJobAddressVo().getDetailAddress(),
                        jobPostingEntity.getJobAddressVo().getExtraAddress()
                        ),
                jobPostingEntity.getIsActive(),
                jobPostingEntity.getStartDate(),
                jobPostingEntity.getEndDate()
        );
    }

    private JobPostingEntity convert(JobPosting jobPosting) {
        return JobPostingEntity
                .builder()
                .id(jobPosting.getId())
                .memberId(jobPosting.getMemberId())
                .title(jobPosting.getTitle())
                .requirementLicense(jobPosting.getRequirementLicense())
                .requirementEtc(jobPosting.getRequirementEtc())
                .jobAddressVo(new JobAddressVo(
                        jobPosting.getAddress().getPostcode(),
                        jobPosting.getAddress().getRoadAddress(),
                        jobPosting.getAddress().getJibunAddress(),
                        jobPosting.getAddress().getRoadAddress(),
                        jobPosting.getAddress().getExtraAddress()
                        ))
                .isActive(jobPosting.getIsActive())
                .startDate(jobPosting.getStartDate())
                .endDate(jobPosting.getEndDate())
                .memberId(jobPosting.getMemberId())
                .build();
    }

    private Member convert(MemberEntity memberEntity) {
        return new Member(
                memberEntity.getId(),
                memberEntity.getEmail(),
                memberEntity.getPassword(),
                memberEntity.getName(),
                memberEntity.getAge(),
                memberEntity.getGender()
        );
    }

    private MemberEntity convert(Member member) {
        return MemberEntity
                .builder()
                .id(member.getId())
                .email(member.getEmail())
                .password(member.getPassword())
                .name(member.getName())
                .age(member.getAge())
                .gender(member.getGender())
                .build();
    }
}
