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

    /**
     * 공고의 식별자와 회원의 식별자를 사용하여 해당 공고가 해당 회원에 의해 소유되었는지를 확인합니다.
     *
     * @param id       공고의 식별자입니다.
     * @param memberId 회원의 식별자입니다.
     * @return 공고가 해당 회원에 의해 소유되었는 경우 true를 반환하고, 그렇지 않으면 false를 반환합니다.
     */
    @Override
    public boolean existsByIdAndMemberId(Long id, Long memberId) {
        return jobJpaRepository.existsByIdAndMemberId(id, memberId);
    }

    /**
     * 공고의 식별자를 사용하여 해당 공고를 조회합니다.
     *
     * @param id 공고의 식별자입니다.
     * @return 조회된 공고 객체를 담은 Optional입니다. 공고가 존재하지 않는 경우 Optional.empty()를 반환합니다.
     */
    @Override
    public Optional<Job> findById(Long id) {
        Optional<JobEntity> jobEntity = jobJpaRepository.findById(id);
        return jobEntity.map(this::convert);
    }

    /**
     * 회원의 식별자를 사용하여 해당 회원이 생성한 공고 목록을 페이지로 조회합니다.
     *
     * @param memberId     회원의 식별자입니다.
     * @param pageRequest  페이지 요청 정보입니다.
     * @return 페이지로 나뉘어진 공고 목록입니다.
     */
    @Override
    public Page<Job> findByMemberId(Long memberId, PageRequest pageRequest) {
        Page<JobEntity> jobEntities = jobJpaRepository.findByMemberId(memberId, pageRequest);
        return jobEntities.map(this::convert);
    }

    /**
     * 키워드를 포함하는 공고 목록을 페이지로 조회합니다.
     *
     * @param keyword      검색할 키워드입니다.
     * @param pageRequest  페이지 요청 정보입니다.
     * @return 페이지로 나뉘어진 공고 목록입니다.
     */
    @Override
    public Page<Job> findByLikeKeyword(String keyword, PageRequest pageRequest) {
        Page<JobEntity> jobEntities = jobJpaRepository.findByLikeKeyword(keyword, pageRequest);
        return jobEntities.map(this::convert);
    }

    /**
     * 공고를 저장합니다.
     *
     * @param job 저장할 공고 객체입니다.
     * @return 저장된 공고 객체입니다.
     */
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
