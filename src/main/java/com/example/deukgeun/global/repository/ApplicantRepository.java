package com.example.deukgeun.global.repository;

import com.example.deukgeun.global.entity.Applicant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicantRepository extends JpaRepository<Applicant, Long> {
}
