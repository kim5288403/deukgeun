package com.example.deukgeun.authToken.infrastructure.persistence.mapper;

import com.example.deukgeun.authToken.domain.model.entity.AuthToken;
import com.example.deukgeun.authToken.infrastructure.persistence.entity.AuthTokenEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthTokenMapper {
    AuthToken toAuthToken(AuthTokenEntity authTokenEntity);
    AuthTokenEntity toAuthTokenEntity(AuthToken authToken);
}
