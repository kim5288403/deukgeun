package com.example.deukgeun.applicant.application.service.implement;

import com.example.deukgeun.applicant.application.dto.request.SaveMatchInfoRequest;
import com.example.deukgeun.applicant.application.service.MatchApplicationService;
import com.example.deukgeun.applicant.domain.dto.SaveMatchInfoDTO;
import com.example.deukgeun.applicant.domain.model.aggregate.Applicant;
import com.example.deukgeun.applicant.domain.service.ApplicantDomainService;
import com.example.deukgeun.applicant.infrastructure.persistence.mapper.MatchMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;

@Service
@RequiredArgsConstructor
public class MatchApplicationServiceImpl implements MatchApplicationService {
    private final ApplicantDomainService applicantDomainService;
    private final MatchMapper matchMapper;

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
     * SaveMatchInfoRequest, 상태(status) 정보를 사용하여 지원자와 매칭하는 메서드입니다.
     *
     * @param saveMatchInfoRequest 매칭 정보를 저장하는 데 사용되는 요청 객체
     * @param status 매칭 상태를 나타내는 정수 값
     * @return 매칭된 지원자 정보를 포함하는 Applicant 객체
     */
    @Override
    public Applicant saveMatchInfo(SaveMatchInfoRequest saveMatchInfoRequest, int status) {
        SaveMatchInfoDTO saveMatchInfoDTO = matchMapper.toSaveMatchInfoDto(status, saveMatchInfoRequest);

        return applicantDomainService.saveMatchInfo(saveMatchInfoDTO);
    }

}
