package com.example.deukgeun.job.domain.service.implement;

import com.example.deukgeun.job.application.dto.request.SaveJobRequest;
import com.example.deukgeun.job.domain.model.aggregate.Job;
import com.example.deukgeun.job.domain.repository.JobRepository;
import com.example.deukgeun.job.domain.service.JobDomainService;
import com.example.deukgeun.trainer.domain.model.valueobjcet.Address;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class JobDomainServiceImpl implements JobDomainService {
    private final JobRepository jobRepository;

    @Override
    public boolean existsByIdAndMemberId(Long id, Long memberId) {
        return jobRepository.existsByIdAndMemberId(id, memberId);
    }

    @Override
    public Job findById(Long id) {
        return jobRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("없는 정보 입니다.")
        );
    }

    @Override
    public Page<Job> getListByMemberId(Long memberId, PageRequest pageRequest) {
        return jobRepository.findByMemberId(memberId, pageRequest);
    }

    @Override
    public Page<Job> getListByKeyword(String keyword, PageRequest pageRequest) {
        return jobRepository.findByLikeKeyword(keyword, pageRequest);
    }

    @Override
    public Job save(SaveJobRequest saveJobRequest, Long memberId) {

        Job job = Job.create(
                memberId,
                saveJobRequest.getTitle(),
                saveJobRequest.getRequirementLicense(),
                saveJobRequest.getRequirementEtc(),
                new Address(
                        saveJobRequest.getPostcode(),
                        saveJobRequest.getJibunAddress(),
                        saveJobRequest.getRoadAddress(),
                        saveJobRequest.getDetailAddress(),
                        saveJobRequest.getExtraAddress()
                ),
                1,
                saveJobRequest.getStartDate(),
                saveJobRequest.getEndDate()
        );

        return jobRepository.save(job);
    }

    @Override
    public Job updateIsActiveByJobId(int isActive, Long id) {
        Job job = findById(id);
        job.updateIsActive(isActive);
        return jobRepository.save(job);
    }
}
