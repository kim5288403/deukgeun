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
import com.example.deukgeun.job.domain.model.aggregate.Job;
import com.example.deukgeun.job.infrastructure.persistence.model.entity.JobEntity;
import com.example.deukgeun.job.infrastructure.persistence.model.valueobject.JobAddressVo;
import com.example.deukgeun.trainer.domain.model.valueobjcet.Address;
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
    public boolean existsByJobIdAndMatchInfoIdNotNull(Long jobId) {
        return applicantJpaRepository.existsByJobIdAndMatchInfoIdNotNull(jobId);
    }

    @Override
    public boolean existsByJobIdAndTrainerId(Long jobId, Long trainerId) {
        return applicantJpaRepository.existsByJobIdAndTrainerId(jobId, trainerId);
    }

    @Override
    public Optional<Applicant> findById(Long id) {
        Optional<ApplicantEntity> applicantEntity = applicantJpaRepository.findById(id);
        return applicantEntity.map(this::convert);
    }

    @Override
    public Page<Applicant> findPageByJobId(Long jobId, PageRequest pageRequest) {
        Page<ApplicantEntity> applicantEntities = applicantJpaRepository.findPageByJobId(jobId, pageRequest);
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
                applicantEntity.getJobId(),
                applicantEntity.getMatchInfoId(),
                applicantEntity.getPaymentInfoId(),
                applicantEntity.getTrainerId(),
                applicantEntity.getSupportAmount(),
                applicantEntity.getIsSelected(),
                convert(applicantEntity.getJobEntity()),
                convert(applicantEntity.getMatchInfoEntity()),
                convert(applicantEntity.getPaymentInfoEntity())
        );
    }

    private ApplicantEntity convert(Applicant applicant) {
        return ApplicantEntity
                .builder()
                .id(applicant.getId())
                .jobId(applicant.getJobId())
                .trainerId(applicant.getTrainerId())
                .supportAmount(applicant.getSupportAmount())
                .isSelected(applicant.getIsSelected())
                .jobEntity(convert(applicant.getJob()))
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
                matchInfoEntity.getJobId(),
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
                .jobId(matchInfo.getJobId())
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

    private Job convert(JobEntity jobEntity) {
        return new Job(
                jobEntity.getId(),
                jobEntity.getMemberId(),
                jobEntity.getTitle(),
                jobEntity.getRequirementLicense(),
                jobEntity.getRequirementEtc(),
                new Address(
                        jobEntity.getJobAddressVo().getPostcode(),
                        jobEntity.getJobAddressVo().getJibunAddress(),
                        jobEntity.getJobAddressVo().getRoadAddress(),
                        jobEntity.getJobAddressVo().getDetailAddress(),
                        jobEntity.getJobAddressVo().getExtraAddress()
                ),
                jobEntity.getIsActive(),
                jobEntity.getStartDate(),
                jobEntity.getEndDate()
        );
    }

    private JobEntity convert(Job job) {
        return JobEntity
                .builder()
                .id(job.getId())
                .memberId(job.getMemberId())
                .title(job.getTitle())
                .requirementLicense(job.getRequirementLicense())
                .requirementEtc(job.getRequirementEtc())
                .jobAddressVo(new JobAddressVo(
                        job.getAddress().getPostcode(),
                        job.getAddress().getRoadAddress(),
                        job.getAddress().getJibunAddress(),
                        job.getAddress().getRoadAddress(),
                        job.getAddress().getExtraAddress()
                ))
                .isActive(job.getIsActive())
                .startDate(job.getStartDate())
                .endDate(job.getEndDate())
                .memberId(job.getMemberId())
                .build();
    }
}
