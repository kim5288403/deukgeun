package com.example.deukgeun.member.service.implement;

import com.example.deukgeun.global.entity.MatchInfo;
import com.example.deukgeun.global.repository.MatchInfoRepository;
import com.example.deukgeun.member.request.SaveMatchInfoRequest;
import com.example.deukgeun.member.service.MatchInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("member.service.MatchInfo")
@RequiredArgsConstructor
public class MatchInfoServiceImpl implements MatchInfoService {

    private final MatchInfoRepository matchInfoRepository;
    @Value("${deukgeun.status.paymentWaiting}")
    private Integer PAYMENT_WAITING;

    @Override
    public MatchInfo save(SaveMatchInfoRequest saveMatchInfoRequest) {
        MatchInfo matchInfo = MatchInfo
                .builder()
                .applicantId(saveMatchInfoRequest.getApplicantId())
                .jobPostingId(saveMatchInfoRequest.getJobPostingId())
                .status(PAYMENT_WAITING)
                .build();

        return matchInfoRepository.save(matchInfo);
    }
}
