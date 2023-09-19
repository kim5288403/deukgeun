package com.example.deukgeun.authMail.infrastructure.persistence.mapper;

import com.example.deukgeun.authMail.application.dto.request.AuthMailRequest;
import com.example.deukgeun.authMail.domain.dto.ConfirmDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMailMapper {

    ConfirmDTO toConfirmDto(AuthMailRequest authMailRequest);
}
