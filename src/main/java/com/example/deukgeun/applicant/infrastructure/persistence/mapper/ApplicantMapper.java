package com.example.deukgeun.applicant.infrastructure.persistence.mapper;

import com.example.deukgeun.applicant.application.dto.request.SaveApplicantRequest;
import com.example.deukgeun.applicant.application.dto.response.ApplicantResponse;
import com.example.deukgeun.applicant.domain.dto.SaveApplicantDTO;
import com.example.deukgeun.applicant.domain.model.aggregate.Applicant;
import com.example.deukgeun.applicant.infrastructure.persistence.model.entity.ApplicantEntity;
import com.example.deukgeun.job.domain.model.aggregate.Job;
import com.example.deukgeun.member.domain.aggregate.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(
        componentModel = "spring",
        uses = {MatchMapper.class, PaymentMapper.class}
)
public interface ApplicantMapper {
    ApplicantMapper INSTANCE = Mappers.getMapper(ApplicantMapper.class);

    ApplicantResponse.List toApplicantResponseList(Applicant source);

    SaveApplicantDTO toSaveApplicantDto(Long trainerId, SaveApplicantRequest saveApplicantRequest);

    @Mapping(target = "amount", expression = "java(workDay * source.getSupportAmount())")
    @Mapping(target = "id", source = "source.id")
    @Mapping(target = "title", source = "job.title")
    @Mapping(target = "postcode", source = "job.jobAddress.postcode")
    @Mapping(target = "roadAddress", source = "job.jobAddress.roadAddress")
    @Mapping(target = "email", source = "member.email")
    @Mapping(target = "name", source = "member.name")
    ApplicantResponse.Info toApplicantResponseInfo(Applicant source, Member member, Job job, int workDay);

    @Mapping(source = "matchInfoEntity", target = "matchInfo", qualifiedByName = "toMatchInfo", defaultExpression = "java(null)")
    @Mapping(source = "paymentInfoEntity", target = "paymentInfo", qualifiedByName = "toPaymentInfo", defaultExpression = "java(null)")
    Applicant toApplicant(ApplicantEntity applicantEntity);

    @Mapping(source = "matchInfo", target = "matchInfoEntity", qualifiedByName = "toMatchInfoEntity", defaultExpression = "java(null)")
    @Mapping(source = "paymentInfo", target = "paymentInfoEntity", qualifiedByName = "toPaymentInfoEntity", defaultExpression = "java(null)")
    @Mapping(target = "jobEntity", ignore = true)
    ApplicantEntity toApplicantEntity(Applicant applicant);
}
