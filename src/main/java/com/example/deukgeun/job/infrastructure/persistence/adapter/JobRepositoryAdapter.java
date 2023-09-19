package com.example.deukgeun.job.infrastructure.persistence.adapter;

import com.example.deukgeun.job.domain.model.aggregate.Job;
import com.example.deukgeun.job.domain.repository.JobRepository;
import com.example.deukgeun.job.infrastructure.persistence.mapper.JobMapper;
import com.example.deukgeun.job.infrastructure.persistence.model.entity.JobEntity;
import com.example.deukgeun.job.infrastructure.persistence.repository.JobJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JobRepositoryAdapter implements JobRepository {
    private final JobJpaRepository jobJpaRepository;
    private final JobMapper jobMapper;

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
        return jobEntity.map(jobMapper::toJob);
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
        return jobEntities.map(jobMapper::toJob);
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

        return jobEntities.map(jobMapper::toJob);
    }

    /**
     * 공고를 저장합니다.
     *
     * @param job 저장할 공고 객체입니다.
     * @return 저장된 공고 객체입니다.
     */
    @Override
    public Job save(Job job) {
        JobEntity jobEntity = jobJpaRepository.save(jobMapper.toJobEntity(job));

        return jobMapper.toJob(jobEntity);
    }
}
