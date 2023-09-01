package com.example.deukgeun.applicant.domain.service.implement;

import com.example.deukgeun.applicant.application.dto.request.PaymentInfoRequest;
import com.example.deukgeun.applicant.application.dto.request.SaveApplicantRequest;
import com.example.deukgeun.applicant.application.dto.request.SaveMatchInfoRequest;
import com.example.deukgeun.applicant.application.dto.response.IamPortCancelResponse;
import com.example.deukgeun.applicant.domain.model.aggregate.Applicant;
import com.example.deukgeun.applicant.domain.model.entity.MatchInfo;
import com.example.deukgeun.applicant.domain.model.entity.PaymentCancelInfo;
import com.example.deukgeun.applicant.domain.model.entity.PaymentInfo;
import com.example.deukgeun.applicant.domain.repository.ApplicantRepository;
import com.example.deukgeun.applicant.domain.service.ApplicantDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ApplicantDomainServiceImpl implements ApplicantDomainService {
    private final ApplicantRepository applicantRepository;

    @Override
    public void cancel(Long id, IamPortCancelResponse iamPortCancelResponse) {
        Applicant applicant = findById(id);

        applicant.deletePaymentInfo();

        PaymentCancelInfo paymentCancelInfo = PaymentCancelInfo.create(
                iamPortCancelResponse.getResponse().getImp_uid(),
                iamPortCancelResponse.getResponse().getChannel(),
                iamPortCancelResponse.getResponse().getCancel_reason(),
                iamPortCancelResponse.getResponse().getCancel_amount()
        );

        applicant.getPaymentInfo().setPaymentCancelInfo(paymentCancelInfo);

        applicantRepository.save(applicant);
    }

    @Override
    public void deleteMatchInfoById(Long id) {
        Applicant applicant = findById(id);
        applicant.deleteMatchInfo();
        applicantRepository.save(applicant);
    }

    @Override
    public Applicant findById(Long id) {
        return applicantRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("없는 지원 정보입니다.")
        );
    }

    @Override
    public Page<Applicant> getByJobId(Long jobId, PageRequest pageRequest) {
        return applicantRepository.findPageByJobId(jobId, pageRequest);
    }

    @Override
    public boolean isAnnouncementMatchedByJobId(Long jobId) {
        return applicantRepository.existsByJobIdAndMatchInfoIdNotNull(jobId);
    }

    @Override
    public Applicant matching(SaveMatchInfoRequest saveMatchInfoRequest, int status) {
        Applicant applicant = findById(saveMatchInfoRequest.getApplicantId());

        MatchInfo matchInfo = MatchInfo.create(
                saveMatchInfoRequest.getJobId(),
                status
        );

        applicant.setMatchInfo(matchInfo);

        return applicantRepository.save(applicant);
    }

    @Override
    public Applicant payment(PaymentInfoRequest request, LocalDateTime paidAt) {
        Applicant applicant = findById(request.getApplicantId());
        PaymentInfo paymentInfo = PaymentInfo.create(
                request.getImpUid(),
                request.getPgProvider(),
                request.getPgTid(),
                request.getChannel(),
                request.getAmount(),
                paidAt
        );

        applicant.setPaymentInfo(paymentInfo);
        return applicantRepository.save(applicant);
    }

    @Override
    public Applicant save(SaveApplicantRequest saveApplicantRequest, Long trainerId) {
        if (applicantRepository.existsByJobIdAndTrainerId(saveApplicantRequest.getJobId(), trainerId)) {
            throw new EntityExistsException("이미 지원한 공고 입니다.");
        }

        Applicant applicant = Applicant.create(
                saveApplicantRequest.getJobId(),
                trainerId,
                saveApplicantRequest.getSupportAmount(),
                0
        );

        return applicantRepository.save(applicant);
    }


    @Override
    public void updateIsSelectedById(Long applicantId, int isSelected) {
        Applicant applicant = applicantRepository.findById(applicantId).
                orElseThrow(() -> new EntityNotFoundException("찾을수 없는 정보입니다."));

        applicant.updateIsSelect(isSelected);
        applicantRepository.save(applicant);
    }
}
