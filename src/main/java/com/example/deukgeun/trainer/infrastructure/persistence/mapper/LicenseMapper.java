package com.example.deukgeun.trainer.infrastructure.persistence.mapper;

import com.example.deukgeun.trainer.application.dto.response.LicenseResponse;
import com.example.deukgeun.trainer.domain.dto.SaveLicenseDTO;
import com.example.deukgeun.trainer.domain.model.entity.License;
import com.example.deukgeun.trainer.infrastructure.persistence.model.entity.LicenseEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LicenseMapper {

    @Mapping(target = "licenseId", source = "id")
    LicenseResponse.List toLicenseResponseList(License license);
    SaveLicenseDTO toSaveLicenseDto(String email, LicenseResponse.Result result);

    @Named("toLicenseList")
    List<License> toLicenseList(List<LicenseEntity> licenseEntities);
    @Named("toLicenseEntityList")
    List<LicenseEntity> toLicenseEntityList(List<License> licenses);
}
