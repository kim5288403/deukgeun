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

    /**
     * 지정된 ID를 사용하여 결제를 취소하는 메서드입니다.
     *
     * @param id 결제를 취소할 지원자(ID)의 고유 ID
     * @param iamPortCancelResponse IamPort 결제 취소 응답 객체
     */
    @Override
    public void updatePaymentCancelInfoById(Long id, IamPortCancelResponse iamPortCancelResponse) {
        applicantDomainService.updatePaymentCancelInfoById(id, iamPortCancelResponse);
    }

    /**
     * 지정된 ID를 사용하여 매치 정보를 삭제하는 메서드입니다.
     *
     * @param id 삭제할 매치 정보의 고유 ID
     */
    @Override
    public void deleteMatchInfoById(Long id) {
        applicantDomainService.deleteMatchInfoById(id);
    }

    /**
     * 지정된 ID를 사용하여 지원자 정보를 조회하는 메서드입니다.
     *
     * @param id 조회할 지원자의 고유 ID
     * @return 조회된 지원자 정보를 포함하는 Applicant 객체
     */
    @Override
    public Applicant findById(Long id) {
        return applicantDomainService.findById(id);
    }

    /**
     * 공고 ID를 사용하여 페이지네이션된 지원자 목록을 조회하는 메서드입니다.
     *
     * @param jobId 지원자를 조회할 공고 ID
     * @param currentPage 현재 페이지 번호
     * @return 페이지네이션된 지원자 목록을 포함하는 Page 객체
     */
    @Override
    public Page<ApplicantResponse.List> getByJobId(Long jobId, int currentPage) {
        PageRequest pageRequest = PageRequest.of(currentPage, 10);
        Page<Applicant> applicants = applicantDomainService.getByJobId(jobId, pageRequest);
        return applicants.map(ApplicantResponse.List::new);
    }

    /**
     * 지정된 공고 ID로 공고에 매칭된 지원자가 있는지 확인하는 메서드입니다.
     *
     * @param jobId 공고 ID를 나타내는 고유 ID
     * @throws EntityExistsException 만약 매칭된 지원자가 이미 존재하는 경우 발생하는 예외
     */
    @Override
    public void isAnnouncementMatchedByJobId(Long jobId) {
        boolean isAnnouncement = applicantDomainService.isAnnouncementMatchedByJobId(jobId);

        // 매칭된 지원자가 이미 존재하는 경우 EntityExistsException 발생시킵니다.
        if (isAnnouncement) {
            throw new EntityExistsException("이미 선택한 지원자가 있습니다.");
        }
    }

    /**
     * SaveApplicantRequest 트레이너 ID를 사용하여 지원자 정보를 저장하는 메서드입니다.
     *
     * @param saveApplicantRequest 저장할 지원자 정보를 담은 요청 객체
     * @param trainerId 트레이너의 고유 ID
     * @return 저장된 지원자 정보를 포함하는 Applicant 객체
     */
    @Override
    public Applicant save(SaveApplicantRequest saveApplicantRequest, Long trainerId) {
        return applicantDomainService.save(saveApplicantRequest, trainerId);
    }

    /**
     * PaymentInfoRequest 사용하여 결제 정보를 처리하고 지원자 정보를 반환하는 메서드입니다.
     *
     * @param request 결제 정보를 나타내는 PaymentInfoRequest 객체
     * @return 처리된 지원자 정보를 포함하는 Applicant 객체
     */
    @Override
    public Applicant savePaymentInfo(PaymentInfoRequest request) {
        // 날짜 형식 지정을 위한 DateTimeFormatter 객체 생성
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        // 요청에서 'paidAt' 값을 파싱하여 LocalDateTime 객체로 변환
        LocalDateTime paidAt = LocalDateTime.parse(request.getPaidAt(), formatter);

        return applicantDomainService.savePaymentInfo(request, paidAt);
    }

    /**
     * SaveMatchInfoRequest, 상태(status) 정보를 사용하여 지원자와 매칭하는 메서드입니다.
     *
     * @param saveMatchInfoRequest 매칭 정보를 저장하는 데 사용되는 요청 객체
     * @param status 매칭 상태를 나타내는 정수 값
     * @return 매칭된 지원자 정보를 포함하는 Applicant 객체
     */
    @Override
    public Applicant saveMatchInfo(SaveMatchInfoRequest saveMatchInfoRequest, int status) {
        return applicantDomainService.saveMatchInfo(saveMatchInfoRequest, status);
    }


    /**
     * 고유 지원자 ID와 선택 여부(isSelected) 정보를 사용하여 지원자 정보를 업데이트하는 메서드입니다.
     *
     * @param id 업데이트할 지원자의 고유 ID
     * @param isSelected 새로운 선택 여부 값을 나타내는 정수 값
     */
    @Override
    public void updateIsSelectedById(Long id, int isSelected) {
        applicantDomainService.updateIsSelectedById(id, isSelected);
    }
}
