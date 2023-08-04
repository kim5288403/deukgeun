package com.example.deukgeun.applicant.infrastructure.persistence.adapter;

import com.example.deukgeun.applicant.domain.model.aggregate.Applicant;
import com.example.deukgeun.applicant.domain.model.entity.MatchInfo;
import com.example.deukgeun.applicant.domain.model.entity.PaymentCancelInfo;
import com.example.deukgeun.applicant.domain.model.entity.PaymentInfo;
import com.example.deukgeun.applicant.domain.repository.ApplicantRepository;
import com.example.deukgeun.applicant.infrastructure.persistence.model.entity.ApplicantEntity;
import com.example.deukgeun.applicant.infrastructure.persistence.model.entity.MatchInfoEntity;
import com.example.deukgeun.applicant.infrastructure.persistence.model.entity.PaymentCancelInfoEntity;
import com.example.deukgeun.applicant.infrastructure.persistence.model.entity.PaymentInfoEntity;
import com.example.deukgeun.applicant.infrastructure.persistence.repository.ApplicantJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ApplicantRepositoryAdapter implements ApplicantRepository {
    private final ApplicantJpaRepository applicantJpaRepository;

    @Override
    public boolean existsByJobPostingIdAndMatchInfoIdNotNull(Long jobPostingId) {
        return applicantJpaRepository.existsByJobPostingIdAndMatchInfoIdNotNull(jobPostingId);
    }

    @Override
    public boolean existsByJobPostingIdAndTrainerId(Long jobPositingId, Long trainerId) {
        return applicantJpaRepository.existsByJobPostingIdAndTrainerId(jobPositingId, trainerId);
    }

    @Override
    public Optional<Applicant> findById(Long id) {
        Optional<ApplicantEntity> applicantEntity = applicantJpaRepository.findById(id);
        return applicantEntity.map(this::convert);
    }

    @Override
    public Page<Applicant> findPageByJobPostingId(Long jobPostingId, PageRequest pageRequest) {
        Page<ApplicantEntity> applicantEntities = applicantJpaRepository.findPageByJobPostingId(jobPostingId, pageRequest);
        return applicantEntities.map(this::convert);
    }

    @Override
    public Applicant save(Applicant applicant) {
        ApplicantEntity applicantEntity = applicantJpaRepository.save(convert(applicant));
        return convert(applicantEntity);
    }

    private Applicant convert(ApplicantEntity applicantEntity) {
        return new Applicant(
                applicantEntity.getId(),
                applicantEntity.getJobPostingId(),
                applicantEntity.getMatchInfoId(),
                applicantEntity.getPaymentInfoId(),
                applicantEntity.getTrainerId(),
                applicantEntity.getSupportAmount(),
                applicantEntity.getIsSelected(),
                applicantEntity.getJobPosting(),
                convert(applicantEntity.getMatchInfoEntity()),
                convert(applicantEntity.getPaymentInfoEntity())
        );
    }

    private ApplicantEntity convert(Applicant applicant) {
        return ApplicantEntity
                .builder()
                .id(applicant.getId())
                .jobPostingId(applicant.getJobPostingId())
                .trainerId(applicant.getTrainerId())
                .supportAmount(applicant.getSupportAmount())
                .isSelected(applicant.getIsSelected())
                .jobPosting(applicant.getJobPosting())
                .matchInfoEntity(convert(applicant.getMatchInfo()))
                .matchInfoId(applicant.getMatchInfoId())
                .paymentInfoEntity(convert(applicant.getPaymentInfo()))
                .paymentInfoId(applicant.getPaymentInfoId())
                .build();
    }

    private MatchInfo convert(MatchInfoEntity matchInfoEntity) {
        if (matchInfoEntity == null) {
            return null;
        }
        return new MatchInfo(
                matchInfoEntity.getId(),
                matchInfoEntity.getJobPostingId(),
                matchInfoEntity.getStatus()
        );
    }

    private MatchInfoEntity convert(MatchInfo matchInfo) {
        if (matchInfo == null) {
            return null;
        }
        return MatchInfoEntity
                .builder()
                .id(matchInfo.getId())
                .jobPostingId(matchInfo.getJobPostingId())
                .status(matchInfo.getStatus())
                .build();
    }

    private PaymentInfo convert(PaymentInfoEntity paymentInfoEntity) {
        if (paymentInfoEntity == null) {
            return null;
        }
        return new PaymentInfo(
                paymentInfoEntity.getId(),
                paymentInfoEntity.getImpUid(),
                paymentInfoEntity.getPgProvider(),
                paymentInfoEntity.getPgTid(),
                paymentInfoEntity.getChannel(),
                paymentInfoEntity.getAmount(),
                paymentInfoEntity.getPaidAt(),
                convert(paymentInfoEntity.getPaymentCancelInfoEntity())
        );
    }

    private PaymentInfoEntity convert(PaymentInfo paymentInfo) {
        if (paymentInfo == null) {
            return null;
        }
        return PaymentInfoEntity
                .builder()
                .id(paymentInfo.getId())
                .impUid(paymentInfo.getImpUid())
                .pgProvider(paymentInfo.getPgProvider())
                .pgTid(paymentInfo.getPgTid())
                .channel(paymentInfo.getChannel())
                .amount(paymentInfo.getAmount())
                .paidAt(paymentInfo.getPaidAt())
                .deleteDate(paymentInfo.getDeleteDate())
                .paymentCancelInfoEntity(convert(paymentInfo.getPaymentCancelInfo()))
                .paymentCancelInfoId(paymentInfo.getPaymentCancelInfoId())
                .build();
    }

    private PaymentCancelInfo convert(PaymentCancelInfoEntity paymentCancelInfoEntity) {
        if (paymentCancelInfoEntity == null) {
            return null;
        }
        return new PaymentCancelInfo(
                paymentCancelInfoEntity.getId(),
                paymentCancelInfoEntity.getImpUid(),
                paymentCancelInfoEntity.getChannel(),
                paymentCancelInfoEntity.getCancel_reason(),
                paymentCancelInfoEntity.getCancel_amount()
        );
    }

    private PaymentCancelInfoEntity convert(PaymentCancelInfo paymentCancelInfo) {
        if (paymentCancelInfo == null) {
            return null;
        }
        return PaymentCancelInfoEntity
                .builder()
                .id(paymentCancelInfo.getId())
                .impUid(paymentCancelInfo.getImpUid())
                .channel(paymentCancelInfo.getChannel())
                .cancel_reason(paymentCancelInfo.getCancelReason())
                .cancel_amount(paymentCancelInfo.getCancelAmount())
                .build();
    }
}
