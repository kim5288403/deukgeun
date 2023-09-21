package com.example.deukgeun.applicant.application.service;

import com.example.deukgeun.applicant.application.dto.request.PaymentInfoRequest;
import com.example.deukgeun.applicant.application.dto.request.SaveApplicantRequest;
import com.example.deukgeun.applicant.application.dto.request.SaveMatchInfoRequest;
import com.example.deukgeun.applicant.application.dto.response.ApplicantResponse;
import com.example.deukgeun.applicant.application.dto.response.IamPortCancelResponse;
import com.example.deukgeun.applicant.application.dto.response.PaymentResponse;
import com.example.deukgeun.applicant.domain.model.aggregate.Applicant;
import com.example.deukgeun.job.domain.model.aggregate.Job;
import com.example.deukgeun.member.domain.aggregate.Member;
import org.springframework.data.domain.Page;

public interface ApplicantApplicationService {
    Applicant findById(Long id);
    Page<ApplicantResponse.List> getListByJobId(Long jobId, int currentPage);
    ApplicantResponse.Info getApplicantInfo(Applicant applicant, Member member, Job job);
    Applicant save(SaveApplicantRequest saveApplicantRequest, Long trainerId);
    void updateIsSelectedById(Long id, int isSelected);

}
