package com.example.deukgeun.applicant.domain.repository;

import com.example.deukgeun.applicant.domain.model.aggregate.Applicant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

public interface ApplicantRepository {
    boolean existsByJobIdAndMatchInfoIdNotNull(Long jobId);
    boolean existsByJobIdAndTrainerId(Long jobId, Long trainerId);
    Optional<Applicant> findById(Long id);
    Page<Applicant>  findPageByJobId(Long jobId, PageRequest pageRequest);
    Applicant save(Applicant applicant);

}
