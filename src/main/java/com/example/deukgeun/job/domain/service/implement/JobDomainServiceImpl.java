package com.example.deukgeun.job.domain.service.implement;

import com.example.deukgeun.job.domain.dto.SaveJobDTO;
import com.example.deukgeun.job.domain.model.aggregate.Job;
import com.example.deukgeun.job.domain.model.valueobject.JobAddress;
import com.example.deukgeun.job.domain.repository.JobRepository;
import com.example.deukgeun.job.domain.service.JobDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class JobDomainServiceImpl implements JobDomainService {
    private final JobRepository jobRepository;

    /**
     * 공고의 식별자와 회원의 식별자를 사용하여 공고가 해당 회원에 의해 소유되었는지를 확인합니다.
     *
     * @param id       공고의 식별자입니다.
     * @param memberId 회원의 식별자입니다.
     * @return 공고가 해당 회원에 의해 소유되었는 경우 true, 그렇지 않으면 false를 반환합니다.
     */
    @Override
    public boolean existsByIdAndMemberId(Long id, Long memberId) {
        return jobRepository.existsByIdAndMemberId(id, memberId);
    }

    /**
     * 공고의 식별자를 사용하여 해당 공고를 조회합니다.
     *
     * @param id 공고의 식별자입니다.
     * @return 조회된 공고 객체입니다.
     * @throws EntityNotFoundException 공고가 존재하지 않을 경우 발생하는 예외입니다.
     */
    @Override
    public Job findById(Long id) {
        return jobRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("없는 정보 입니다.")
        );
    }

    /**
     * 회원의 식별자와 페이지 요청 정보를 사용하여 해당 회원의 공고 목록을 페이징하여 조회합니다.
     *
     * @param memberId     회원의 식별자입니다.
     * @param pageRequest  페이지 요청 정보입니다.
     * @return 회원의 공고 목록을 페이징한 페이지 객체입니다.
     */
    @Override
    public Page<Job> findListByMemberId(Long memberId, PageRequest pageRequest) {
        return jobRepository.findByMemberId(memberId, pageRequest);
    }

    /**
     * 특정 키워드와 페이지 요청 정보를 사용하여 공고 목록을 키워드에 따라 페이징하여 조회합니다.
     *
     * @param keyword      검색할 키워드입니다.
     * @param pageRequest  페이지 요청 정보입니다.
     * @return 키워드에 따라 페이징한 공고 목록을 담은 페이지 객체입니다.
     */
    @Override
    public Page<Job> findListByKeyword(String keyword, PageRequest pageRequest) {
        return jobRepository.findByLikeKeyword(keyword, pageRequest);
    }

    /**
     * 공고 저장합니다.
     *
     * @param saveJobDTO 공고 정보를 담은 요청 객체입니다.
     * @return 저장된 공고 객체입니다.
     */
    @Override
    public Job save(SaveJobDTO saveJobDTO) {
        Job job = Job.create(
                saveJobDTO.getMemberId(),
                saveJobDTO.getTitle(),
                saveJobDTO.getRequirementLicense(),
                saveJobDTO.getRequirementEtc(),
                saveJobDTO.getJobAddress(),
                1,
                saveJobDTO.getStartDate(),
                saveJobDTO.getEndDate()
        );

        return jobRepository.save(job);
    }

    /**
     * 공고의 활성 상태를 업데이트합니다.
     *
     * @param isActive 공고의 활성 상태를 나타내는 값입니다. (1: 활성, 0: 비활성)
     * @param id       공고의 식별자입니다.
     * @return 업데이트된 공고의 객체입니다.
     */
    @Override
    public Job updateIsActiveByJobId(int isActive, Long id) {
        Job job = findById(id);
        job.updateIsActive(isActive);
        return jobRepository.save(job);
    }
}
