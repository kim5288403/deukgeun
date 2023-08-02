package com.example.deukgeun.applicant.application.service.implement;

import com.example.deukgeun.applicant.application.dto.request.SaveApplicantRequest;
import com.example.deukgeun.applicant.application.dto.response.ApplicantResponse;
import com.example.deukgeun.applicant.application.service.ApplicantApplicationService;
import com.example.deukgeun.applicant.domain.model.aggregate.Applicant;
import com.example.deukgeun.applicant.domain.service.ApplicantDomainService;
import com.example.deukgeun.applicant.application.dto.request.SaveMatchInfoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;

@Service
@RequiredArgsConstructor
public class ApplicantApplicationServiceImpl implements ApplicantApplicationService {

    private final ApplicantDomainService applicantDomainService;

    @Value("${deukgeun.status.paymentWaiting}")
    private Integer PAYMENT_WAITING;

    @Override
    public void deleteMatchInfoById(Long id) {
        applicantDomainService.deleteMatchInfoById(id);
    }

    @Override
    public ApplicantResponse.ApplicantInfo findById(Long id) {
        Applicant applicant = applicantDomainService.findById(id);
        return new ApplicantResponse.ApplicantInfo(applicant);
    }

    @Override
    public Page<ApplicantResponse.ListResponse> getByJobPostingId(Long jobPostingId, int currentPage) {
        PageRequest pageRequest = PageRequest.of(currentPage, 10);
        Page<Applicant> applicants = applicantDomainService.getByJobPostingId(jobPostingId, pageRequest);
        return applicants.map(ApplicantResponse.ListResponse::new);
    }

    @Override
    public void isAnnouncementMatchedByJobPostingId(Long jobPostingId) {
        boolean isAnnouncement = applicantDomainService.isAnnouncementMatchedByJobPostingId(jobPostingId);
        if (isAnnouncement) {
            throw new EntityExistsException("이미 선택한 지원자가 있습니다.");
        }
    }

    @Override
    public Applicant matching(SaveMatchInfoRequest saveMatchInfoRequest) {
        return applicantDomainService.matching(saveMatchInfoRequest, PAYMENT_WAITING);
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
