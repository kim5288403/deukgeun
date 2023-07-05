package com.example.deukgeun.commom.repository;


import com.example.deukgeun.commom.entity.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {
}
