package com.example.deukgeun.applicant.application.service.implement;

import com.example.deukgeun.applicant.application.dto.request.PaymentInfoRequest;
import com.example.deukgeun.applicant.application.dto.request.SaveApplicantRequest;
import com.example.deukgeun.applicant.application.dto.request.SaveMatchInfoRequest;
import com.example.deukgeun.applicant.application.dto.response.ApplicantResponse;
import com.example.deukgeun.applicant.application.dto.response.IamPortCancelResponse;
import com.example.deukgeun.applicant.application.service.ApplicantApplicationService;
import com.example.deukgeun.applicant.domain.model.aggregate.Applicant;
import com.example.deukgeun.applicant.domain.service.ApplicantDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class ApplicantApplicationServiceImpl implements ApplicantApplicationService {

    private final ApplicantDomainService applicantDomainService;

    @Value("${status.payment.waiting}")
    private Integer PAYMENT_WAITING;

    @Override
    public void cancel(Long id, IamPortCancelResponse iamPortCancelResponse) {
        applicantDomainService.cancel(id, iamPortCancelResponse);
    }

    @Override
    public void deleteMatchInfoById(Long id) {
        applicantDomainService.deleteMatchInfoById(id);
    }

    @Override
    public Applicant findById(Long id) {
        return applicantDomainService.findById(id);
    }

    @Override
    public Page<ApplicantResponse.ListResponse> getByJobId(Long jobId, int currentPage) {
        PageRequest pageRequest = PageRequest.of(currentPage, 10);
        Page<Applicant> applicants = applicantDomainService.getByJobId(jobId, pageRequest);
        return applicants.map(ApplicantResponse.ListResponse::new);
    }

    @Override
    public void isAnnouncementMatchedByJobId(Long jobId) {
        boolean isAnnouncement = applicantDomainService.isAnnouncementMatchedByJobId(jobId);
        if (isAnnouncement) {
            throw new EntityExistsException("이미 선택한 지원자가 있습니다.");
        }
    }

    @Override
    public Applicant matching(SaveMatchInfoRequest saveMatchInfoRequest) {
        return applicantDomainService.matching(saveMatchInfoRequest, PAYMENT_WAITING);
    }

    @Override
    public Applicant payment(PaymentInfoRequest request) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        LocalDateTime paidAt = LocalDateTime.parse(request.getPaidAt(), formatter);
        return applicantDomainService.payment(request, paidAt);
    }

    @Override
    public Applicant save(SaveApplicantRequest saveApplicantRequest, Long trainerId) {
        return applicantDomainService.save(saveApplicantRequest, trainerId);
    }

    @Override
    public void updateIsSelectedById(Long applicantId, int isSelected) {
        applicantDomainService.updateIsSelectedById(applicantId, isSelected);
    }
}
