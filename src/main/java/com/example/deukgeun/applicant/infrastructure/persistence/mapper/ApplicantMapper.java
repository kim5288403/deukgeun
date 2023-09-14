package com.example.deukgeun.applicant.infrastructure.persistence.mapper;

import com.example.deukgeun.applicant.application.dto.request.SaveApplicantRequest;
import com.example.deukgeun.applicant.application.dto.response.ApplicantResponse;
import com.example.deukgeun.applicant.domain.dto.SaveApplicantDTO;
import com.example.deukgeun.applicant.domain.model.aggregate.Applicant;
import com.example.deukgeun.applicant.infrastructure.persistence.model.entity.ApplicantEntity;
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

    @Mapping(source = "matchInfoEntity", target = "matchInfo", qualifiedByName = "toMatchInfo", defaultExpression = "java(null)")
    @Mapping(source = "paymentInfoEntity", target = "paymentInfo", qualifiedByName = "toPaymentInfo", defaultExpression = "java(null)")
    Applicant toApplicant(ApplicantEntity applicantEntity);

    @Mapping(source = "matchInfo", target = "matchInfoEntity", qualifiedByName = "toMatchInfoEntity", defaultExpression = "java(null)")
    @Mapping(source = "paymentInfo", target = "paymentInfoEntity", qualifiedByName = "toPaymentInfoEntity", defaultExpression = "java(null)")
    @Mapping(target = "jobEntity", ignore = true)
    ApplicantEntity toApplicantEntity(Applicant applicant);
}
