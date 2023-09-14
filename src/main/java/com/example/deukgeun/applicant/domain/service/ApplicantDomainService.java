package com.example.deukgeun.applicant.domain.service;

import com.example.deukgeun.applicant.domain.dto.PaymentCancelInfoDTO;
import com.example.deukgeun.applicant.domain.dto.SaveApplicantDTO;
import com.example.deukgeun.applicant.domain.dto.SaveMatchInfoDTO;
import com.example.deukgeun.applicant.domain.dto.SavePaymentInfoDTO;
import com.example.deukgeun.applicant.domain.model.aggregate.Applicant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface ApplicantDomainService {
    void deleteMatchInfoById(Long id);
    void existsByJobIdAndTrainerId(Long jobId, Long trainerId);
    Applicant findById(Long id);
    Page<Applicant> getByJobId(Long jobId, PageRequest pageRequest);
    boolean isAnnouncementMatchedByJobId(Long jobId);
    Applicant save(SaveApplicantDTO saveApplicantDTO);
    void savePaymentInfo(SavePaymentInfoDTO savePaymentInfoDTO);
    Applicant saveMatchInfo(SaveMatchInfoDTO saveMatchInfoDTO);
    void updatePaymentCancelInfoById(PaymentCancelInfoDTO paymentCancelInfoDTO);
    void updateIsSelectedById(Long applicantId, int isSelected);
}
