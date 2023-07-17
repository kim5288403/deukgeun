package com.example.deukgeun.job.domain.repository;

import com.example.deukgeun.job.domain.entity.Applicant;
import com.example.deukgeun.job.application.dto.response.ApplicantResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicantRepository extends JpaRepository<Applicant, Long> {
    Page<ApplicantResponse.ListResponse> findByJobPostingId(Long jobPostingId, PageRequest pageRequest);

    boolean existsByJobPostingIdAndTrainerId(Long jobPostingId, Long trainerId);

}
