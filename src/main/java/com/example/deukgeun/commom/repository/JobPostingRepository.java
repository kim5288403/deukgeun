package com.example.deukgeun.commom.repository;


import com.example.deukgeun.commom.entity.JobPosting;
import com.example.deukgeun.commom.response.JobPostingResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {

    @Query("select j from JobPosting j where j.title like :keyword or j.jibunAddress like :keyword or j.extraAddress like :keyword or j.detailAddress like :keyword or j.roadAddress like :keyword")
    Page<JobPostingResponse.ListResponse> findByLikeKeyword(@Param(value = "keyword")String keyword, Pageable pageable);
}
