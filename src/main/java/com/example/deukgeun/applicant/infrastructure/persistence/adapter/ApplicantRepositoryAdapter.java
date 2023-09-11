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

    /**
     * 공고 식별자(jobId)로 매칭 정보(matchInfoId)가 있는지 확인합니다.
     *
     * @param jobId 확인하려는 공고의 고유 식별자
     * @return 공고에 매칭 정보가 있으면 true, 그렇지 않으면 false를 반환합니다.
     */
    @Override
    public boolean existsByJobIdAndMatchInfoIdNotNull(Long jobId) {
        return applicantJpaRepository.existsByJobIdAndMatchInfoIdNotNull(jobId);
    }

    /**
     * 공고(jobId)에 대한 지원자가 지정된 트레이너(trainerId)인지 여부를 확인합니다.
     *
     * @param jobId     공고 고유 식별자
     * @param trainerId 트레이너 고유 식별자
     * @return 해당 공고에 대한 트레이너의 지원 여부를 나타내는 boolean 값
     */
    @Override
    public boolean existsByJobIdAndTrainerId(Long jobId, Long trainerId) {
        return applicantJpaRepository.existsByJobIdAndTrainerId(jobId, trainerId);
    }

    /**
     * 지원자 식별자(id)를 사용하여 지원자 정보를 조회합니다.
     *
     * @param id 조회하려는 지원자의 고유 식별자
     * @return 지원자 정보를 포함한 Optional 객체
     */
    @Override
    public Optional<Applicant> findById(Long id) {
        Optional<ApplicantEntity> applicantEntity = applicantJpaRepository.findById(id);
        return applicantEntity.map(this::convert);
    }

    /**
     * 공고 식별자(jobId)로 페이징된 지원자 목록을 조회합니다.
     *
     * @param jobId       공고 고유 식별자
     * @param pageRequest 페이지 요청 정보 (페이지 번호, 페이지 크기 등)
     * @return 페이지에 포함된 지원자 목록
     */
    @Override
    public Page<Applicant> findPageByJobId(Long jobId, PageRequest pageRequest) {
        Page<ApplicantEntity> applicantEntities = applicantJpaRepository.findPageByJobId(jobId, pageRequest);
        return applicantEntities.map(this::convert);
    }

    /**
     * 지원자 정보를 저장합니다.
     *
     * @param applicant 저장할 지원자 정보
     * @return 저장된 지원자 정보
     */
    @Override
    public Applicant save(Applicant applicant) {
        ApplicantEntity applicantEntity = applicantJpaRepository.save(convert(applicant));
        return convert(applicantEntity);
    }

    private Applicant convert(ApplicantEntity applicantEntity) {
        Applicant applicant = new Applicant(
                applicantEntity.getId(),
                applicantEntity.getJobId(),
                applicantEntity.getMatchInfoId(),
                applicantEntity.getSupportAmount(),
                applicantEntity.getIsSelected()
                );
        applicant.setPaymentInfo(convert(applicantEntity.getPaymentInfoEntity()));
        applicant.setMatchInfo(convert(applicantEntity.getMatchInfoEntity()));
        return applicant;
    }

    private ApplicantEntity convert(Applicant applicant) {
        return ApplicantEntity
                .builder()
                .id(applicant.getId())
                .jobId(applicant.getJobId())
                .trainerId(applicant.getTrainerId())
                .supportAmount(applicant.getSupportAmount())
                .isSelected(applicant.getIsSelected())
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
