package com.example.deukgeun.member.service;

import com.example.deukgeun.global.entity.MatchInfo;
import com.example.deukgeun.member.request.SaveMatchInfoRequest;

public interface MatchInfoService {
    MatchInfo save(SaveMatchInfoRequest saveMatchInfoRequest);
}
