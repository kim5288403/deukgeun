package com.example.deukgeun.trainer.infrastructure.persistence.mapper;

import com.example.deukgeun.trainer.application.dto.response.ProfileResponse;
import com.example.deukgeun.trainer.domain.model.entity.Profile;
import com.example.deukgeun.trainer.infrastructure.persistence.model.entity.ProfileEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    ProfileResponse toProfileResponse(Profile profile);

    @Named("toProfile")
    Profile toProfile(ProfileEntity profileEntity);
    @Named("toProfileEntity")
    ProfileEntity toProfileEntity(Profile profile);
}
