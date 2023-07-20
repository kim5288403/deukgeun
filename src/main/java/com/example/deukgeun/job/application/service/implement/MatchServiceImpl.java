package com.example.deukgeun.job.application.service.implement;

import com.example.deukgeun.job.application.dto.request.SaveMatchInfoRequest;
import com.example.deukgeun.job.application.service.MatchService;
import com.example.deukgeun.job.domain.entity.MatchInfo;
import com.example.deukgeun.job.domain.repository.MatchInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

@Service("member.service.MatchInfo")
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {

    private final MatchInfoRepository matchInfoRepository;
    @Value("${deukgeun.status.paymentWaiting}")
    private Integer PAYMENT_WAITING;

    @Override
    public MatchInfo save(SaveMatchInfoRequest saveMatchInfoRequest) {
        MatchInfo matchInfo = MatchInfo
                .builder()
                .applicantId(saveMatchInfoRequest.getApplicantId())
                .jobPostingId(saveMatchInfoRequest.getJobPostingId())
                .status(this.PAYMENT_WAITING)
                .build();

        return matchInfoRepository.save(matchInfo);
    }

    @Override
    public void deleteByApplicantId(Long applicantId) {
        MatchInfo matchInfo = matchInfoRepository.findByApplicantIdAndDeleteDateIsNull(applicantId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 매칭 정보 입니다."));
        matchInfo.delete();
        matchInfoRepository.save(matchInfo);
    }

    @Override
    public void isAnnouncementMatchedByJobPostingId(Long jobPostingId) {
        if (matchInfoRepository.existsByJobPostingIdAndDeleteDateIsNull(jobPostingId)) {
            throw new EntityExistsException("이미 선택한 지원자가 있습니다.");
        }
    }

}
