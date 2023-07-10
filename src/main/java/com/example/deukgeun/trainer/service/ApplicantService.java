package com.example.deukgeun.trainer.service;

import com.example.deukgeun.global.entity.Applicant;
import com.example.deukgeun.trainer.request.SaveApplicantRequest;

public interface ApplicantService {
    Applicant save(SaveApplicantRequest saveApplicantRequest, Long trainerId);
}
