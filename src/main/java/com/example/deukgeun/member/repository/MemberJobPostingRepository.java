package com.example.deukgeun.member.repository;


import com.example.deukgeun.commom.entity.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJobPostingRepository extends JpaRepository<JobPosting, Long> {

}
