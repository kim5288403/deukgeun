package com.example.deukgeun.job.infrastructure.persistence.mapper;

import com.example.deukgeun.job.application.dto.request.SaveJobRequest;
import com.example.deukgeun.job.application.dto.response.JobResponse;
import com.example.deukgeun.job.domain.dto.SaveJobDTO;
import com.example.deukgeun.job.domain.model.aggregate.Job;
import com.example.deukgeun.job.domain.model.valueobject.JobAddress;
import com.example.deukgeun.job.infrastructure.persistence.model.entity.JobEntity;
import com.example.deukgeun.job.infrastructure.persistence.model.valueobject.JobAddressVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface JobMapper {
    SaveJobDTO toSaveJobDto(Long memberId, SaveJobRequest saveJobRequest);

    @Mapping(target = "address",
            expression = "java(String.join(\" \", job.getJobAddress().getPostcode(), job.getJobAddress().getDetailAddress(), job.getJobAddress().getRoadAddress(), job.getJobAddress().getJibunAddress(), job.getJobAddress().getExtraAddress()))")
    @Mapping(target = "startDate", dateFormat = "yyyy-MM-dd HH:mm")
    @Mapping(target = "endDate", dateFormat = "yyyy-MM-dd HH:mm")
    JobResponse.List toJobResponseList(Job job);

    JobResponse.Detail toJobResponseDetail(Job job);

    @Mapping(source = "jobAddressVo", target = "jobAddress", qualifiedByName = "toJobAddress")
    Job toJob(JobEntity jobEntity);

    @Mapping(source = "jobAddress", target = "jobAddressVo", qualifiedByName = "toJobAddressVo")
    JobEntity toJobEntity(Job job);

    @Named("toJobAddress")
    JobAddress toJobAddress(JobAddressVo jobAddressVo);

    JobAddress toJobAddress(SaveJobDTO saveJobDTO);

    @Named("toJobAddressVo")
    JobAddressVo toJobAddressVo(JobAddress jobAddress);
}
