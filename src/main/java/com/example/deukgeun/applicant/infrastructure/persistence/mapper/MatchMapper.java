package com.example.deukgeun.applicant.infrastructure.persistence.mapper;

import com.example.deukgeun.applicant.application.dto.request.SaveMatchInfoRequest;
import com.example.deukgeun.applicant.domain.dto.SaveMatchInfoDTO;
import com.example.deukgeun.applicant.domain.model.entity.MatchInfo;
import com.example.deukgeun.applicant.infrastructure.persistence.model.entity.MatchInfoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface MatchMapper {
    SaveMatchInfoDTO toSaveMatchInfoDto(int status, SaveMatchInfoRequest saveMatchInfoRequest);

    @Named("toMatchInfo")
    MatchInfo toMatchInfo(MatchInfoEntity matchInfoEntity);

    @Named("toMatchInfoEntity")
    MatchInfoEntity toMatchInfoEntity(MatchInfo matchInfo);
}
