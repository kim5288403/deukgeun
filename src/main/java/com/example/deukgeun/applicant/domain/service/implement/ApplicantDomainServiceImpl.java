package com.example.deukgeun.applicant.domain.service.implement;

import com.example.deukgeun.applicant.domain.dto.PaymentCancelInfoDTO;
import com.example.deukgeun.applicant.domain.dto.SaveApplicantDTO;
import com.example.deukgeun.applicant.domain.dto.SaveMatchInfoDTO;
import com.example.deukgeun.applicant.domain.dto.SavePaymentInfoDTO;
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

@Service
@RequiredArgsConstructor
public class ApplicantDomainServiceImpl implements ApplicantDomainService {
    private final ApplicantRepository applicantRepository;


    /**
     * 고유 지원자 ID를 사용하여 해당 지원자의 매칭 정보를 삭제하는 메서드입니다.
     *
     * @param id 지원자의 고유 ID
     */
    @Override
    public void deleteMatchInfoById(Long id) {
        // 고유 ID를 사용하여 지원자 정보 조회
        Applicant applicant = findById(id);

        // 지원자의 매칭 정보 삭제
        applicant.deleteMatchInfo();

        // 업데이트된 지원자 정보 저장
        applicantRepository.save(applicant);
    }

    /**
     * 지정된 공고 ID와 트레이너 ID로 이미 지원한 경우 {@link EntityExistsException}을 발생시킵니다.
     *
     * @param jobId     지원 확인할 공고 ID
     * @param trainerId 지원 확인할 트레이너 ID
     * @throws EntityExistsException 이미 지원한 경우 발생하는 예외
     */
    @Override
    public void existsByJobIdAndTrainerId(Long jobId, Long trainerId) {
        if (applicantRepository.existsByJobIdAndTrainerId(jobId, trainerId)) {
            throw new EntityExistsException("이미 지원한 공고 입니다.");
        }
    }

    /**
     * 고유 지원자 ID를 사용하여 해당 지원자 정보를 조회하는 메서드입니다.
     *
     * @param id 지원자의 고유 ID
     * @return 조회된 지원자 정보를 포함하는 Applicant 객체
     * @throws EntityNotFoundException 만약 해당 ID에 대한 정보를 찾을 수 없는 경우 발생하는 예외
     */
    @Override
    public Applicant findById(Long id) {
        return applicantRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("없는 지원 정보입니다.")
        );
    }

    /**
     * 지정된 직무 ID를 사용하여 페이지네이션된 지원자 목록을 조회하는 메서드입니다.
     *
     * @param jobId 조회할 공고 ID
     * @param pageRequest 페이지네이션 정보를 담은 PageRequest 객체
     * @return 페이지네이션된 지원자 목록을 포함하는 Page 객체
     */
    @Override
    public Page<Applicant> findPageByJobId(Long jobId, PageRequest pageRequest) {
        return applicantRepository.findPageByJobId(jobId, pageRequest);
    }

    /**
     * 지정된 공고 ID에 매칭된 지원자가 있는지 확인하는 메서드입니다.
     *
     * @param jobId 확인할 공고 ID
     * @return 매칭된 지원자가 존재하면 true, 그렇지 않으면 false 반환합니다.
     */
    @Override
    public boolean isAnnouncementMatchedByJobId(Long jobId) {
        return applicantRepository.existsByJobIdAndMatchInfoIdNotNull(jobId);
    }


    /**
     * SaveApplicantRequest, 트레이너 ID를 사용하여 지원자 정보를 저장하는 메서드입니다.
     *
     * @param saveApplicantDTO 저장할 지원자 정보를 담은 요청 객체
     * @return 저장된 지원자 정보를 포함하는 Applicant 객체
     */
    @Override
    public Applicant save(SaveApplicantDTO saveApplicantDTO) {

        // 새로운 지원자 정보 생성
        Applicant applicant = Applicant.create(
                saveApplicantDTO.getJobId(),
                saveApplicantDTO.getTrainerId(),
                saveApplicantDTO.getSupportAmount(),
                0
        );

        // 업데이트된 지원자 정보 저장 후 반환
        return applicantRepository.save(applicant);
    }


    @Override
    public void savePaymentInfo(SavePaymentInfoDTO savePaymentInfoDTO) {
        // 지원자 ID를 사용하여 해당 지원자 정보 조회
        Applicant applicant = findById(savePaymentInfoDTO.getApplicantId());

        // 결제 정보 생성
        PaymentInfo paymentInfo = PaymentInfo.create(
                savePaymentInfoDTO.getImpUid(),
                savePaymentInfoDTO.getPgProvider(),
                savePaymentInfoDTO.getPgTid(),
                savePaymentInfoDTO.getChannel(),
                savePaymentInfoDTO.getAmount(),
                savePaymentInfoDTO.getPaidAt()
        );

        // 지원자에게 결제 정보 설정
        applicant.setPaymentInfo(paymentInfo);

        // 업데이트된 지원자 정보 저장 후 반환
        applicantRepository.save(applicant);
    }

    /**
     * SaveMatchInfoRequest, 상태(status) 정보를 사용하여 지원자와 매칭 정보를 업데이트하고 저장하는 메서드입니다.
     *
     * @param saveMatchInfoDTO 매칭 정보를 저장하는 데 사용되는 요청 객체
     * @return 업데이트된 지원자 정보를 포함하는 Applicant 객체
     */
    @Override
    public Applicant saveMatchInfo(SaveMatchInfoDTO saveMatchInfoDTO ) {
        // 지원자 ID를 사용하여 해당 지원자 정보 조회
        Applicant applicant = findById(saveMatchInfoDTO.getApplicantId());

        // 새로운 매칭 정보 생성
        MatchInfo matchInfo = MatchInfo.create(
                saveMatchInfoDTO.getStatus()
        );

        // 지원자에게 새로운 매칭 정보 설정
        applicant.setMatchInfo(matchInfo);

        // 업데이트된 지원자 정보 저장 후 반환
        return applicantRepository.save(applicant);
    }

    /**
     * 고유 ID를 사용하여 지원자의 결제 취소 정보를 업데이트합니다.
     *
     * @param paymentCancelInfoDTO 결제 취소 응답 정보
     */
    @Override
    public void updatePaymentCancelInfoById(PaymentCancelInfoDTO paymentCancelInfoDTO) {
        // 고유 ID를 사용하여 지원자 정보 조회
        Applicant applicant = findById(paymentCancelInfoDTO.getApplicantId());

        // 결제 취소 정보 생성 및 설정
        PaymentCancelInfo paymentCancelInfo = PaymentCancelInfo.create(
                paymentCancelInfoDTO.getImp_uid(),
                paymentCancelInfoDTO.getChannel(),
                paymentCancelInfoDTO.getCancel_reason(),
                paymentCancelInfoDTO.getCancel_amount()
        );

        // 지원자의 결제 취소 정보 업데이트
        applicant.getPaymentInfo().setPaymentCancelInfo(paymentCancelInfo);

        // 업데이트된 지원자 정보 저장
        applicantRepository.save(applicant);
    }


    /**
     * 고유 지원자 ID를 사용하여 해당 지원자의 선택 여부(isSelected) 정보를 업데이트하는 메서드입니다.
     *
     * @param id 업데이트할 지원자의 고유 ID
     * @param isSelected 새로운 선택 여부 값을 나타내는 정수 값
     */
    @Override
    public void updateIsSelectedById(Long id, int isSelected) {
        // 고유 ID를 사용하여 지원자 정보 조회
        Applicant applicant = findById(id);

        // 선택 여부(isSelected) 업데이트
        applicant.updateIsSelect(isSelected);

        // 업데이트된 지원자 정보 저장
        applicantRepository.save(applicant);
    }
}
