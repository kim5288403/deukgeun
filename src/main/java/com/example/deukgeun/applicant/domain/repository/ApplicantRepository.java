package com.example.deukgeun.applicant.domain.repository;

import com.example.deukgeun.applicant.domain.model.aggregate.Applicant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

public interface ApplicantRepository {
    boolean existsByJobPostingIdAndMatchInfoIdNotNull(Long jobPostingId);
    boolean existsByJobPostingIdAndTrainerId(Long jobPositingId, Long trainerId);
    Optional<Applicant> findById(Long id);
    Page<Applicant>  findPageByJobPostingId(Long jobPostingId, PageRequest pageRequest);
    Applicant save(Applicant applicant);

}
