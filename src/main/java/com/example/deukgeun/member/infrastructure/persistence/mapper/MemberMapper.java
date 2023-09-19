package com.example.deukgeun.member.infrastructure.persistence.mapper;

import com.example.deukgeun.global.util.PasswordEncoderUtil;
import com.example.deukgeun.member.application.dto.request.JoinRequest;
import com.example.deukgeun.member.domain.aggregate.Member;
import com.example.deukgeun.member.domain.dto.MemberJoinDTO;
import com.example.deukgeun.member.infrastructure.persistence.entity.MemberEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", imports = PasswordEncoderUtil.class)
public interface MemberMapper {
    @Mapping(target = "password", expression = "java(PasswordEncoderUtil.encode(request.getPassword()))")
    MemberJoinDTO toMemberJoinDto(JoinRequest request);
    Member toMember(MemberEntity memberEntity);
    MemberEntity toMemberEntity(Member member);
}
