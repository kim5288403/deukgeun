package com.example.deukgeun.job.infrastructure.persistence.mapper;

import com.example.deukgeun.job.application.dto.request.SaveJobRequest;
import com.example.deukgeun.job.domain.dto.SaveJobDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JobMapper {

    SaveJobDTO toSaveJobDto(Long memberId, SaveJobRequest saveJobRequest);
}
