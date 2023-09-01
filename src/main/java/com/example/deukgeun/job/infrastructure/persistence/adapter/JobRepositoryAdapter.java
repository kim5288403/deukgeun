package com.example.deukgeun.job.infrastructure.persistence.adapter;

import com.example.deukgeun.job.domain.model.aggregate.Job;
import com.example.deukgeun.job.domain.repository.JobRepository;
import com.example.deukgeun.job.infrastructure.persistence.model.entity.JobEntity;
import com.example.deukgeun.job.infrastructure.persistence.model.valueobject.JobAddressVo;
import com.example.deukgeun.job.infrastructure.persistence.repository.JobJpaRepository;
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
public class JobRepositoryAdapter implements JobRepository {
    private final JobJpaRepository jobJpaRepository;

    @Override
    public boolean existsByIdAndMemberId(Long id, Long memberId) {
        return jobJpaRepository.existsByIdAndMemberId(id, memberId);
    }

    @Override
    public Optional<Job> findById(Long id) {
        Optional<JobEntity> jobEntity = jobJpaRepository.findById(id);
        return jobEntity.map(this::convert);
    }

    @Override
    public Page<Job> findByMemberId(Long memberId, PageRequest pageRequest) {
        Page<JobEntity> jobEntities = jobJpaRepository.findByMemberId(memberId, pageRequest);
        return jobEntities.map(this::convert);
    }

    @Override
    public Page<Job> findByLikeKeyword(String keyword, PageRequest pageRequest) {
        Page<JobEntity> jobEntities = jobJpaRepository.findByLikeKeyword(keyword, pageRequest);
        return jobEntities.map(this::convert);
    }

    @Override
    public Job save(Job job) {
        JobEntity jobEntity = jobJpaRepository.save(convert(job));

        return convert(jobEntity);
    }

    private Job convert(JobEntity jobEntity) {
        return new Job(
                jobEntity.getId(),
                jobEntity.getMemberId(),
                jobEntity.getTitle(),
                jobEntity.getRequirementLicense(),
                jobEntity.getRequirementEtc(),
                new Address(
                        jobEntity.getJobAddressVo().getPostcode(),
                        jobEntity.getJobAddressVo().getJibunAddress(),
                        jobEntity.getJobAddressVo().getRoadAddress(),
                        jobEntity.getJobAddressVo().getDetailAddress(),
                        jobEntity.getJobAddressVo().getExtraAddress()
                        ),
                jobEntity.getIsActive(),
                jobEntity.getStartDate(),
                jobEntity.getEndDate()
        );
    }

    private JobEntity convert(Job job) {
        return JobEntity
                .builder()
                .id(job.getId())
                .memberId(job.getMemberId())
                .title(job.getTitle())
                .requirementLicense(job.getRequirementLicense())
                .requirementEtc(job.getRequirementEtc())
                .jobAddressVo(new JobAddressVo(
                        job.getAddress().getPostcode(),
                        job.getAddress().getRoadAddress(),
                        job.getAddress().getJibunAddress(),
                        job.getAddress().getRoadAddress(),
                        job.getAddress().getExtraAddress()
                        ))
                .isActive(job.getIsActive())
                .startDate(job.getStartDate())
                .endDate(job.getEndDate())
                .memberId(job.getMemberId())
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
