package com.example.deukgeun.trainer.infrastructure.persistence.mapper;

import com.example.deukgeun.global.util.PasswordEncoderUtil;
import com.example.deukgeun.trainer.application.dto.request.JoinRequest;
import com.example.deukgeun.trainer.application.dto.request.UpdateInfoRequest;
import com.example.deukgeun.trainer.application.dto.request.UpdatePasswordRequest;
import com.example.deukgeun.trainer.application.dto.response.TrainerResponse;
import com.example.deukgeun.trainer.domain.dto.SaveTrainerDTO;
import com.example.deukgeun.trainer.domain.dto.UpdateInfoDTO;
import com.example.deukgeun.trainer.domain.dto.UpdatePasswordDTO;
import com.example.deukgeun.trainer.domain.model.aggregate.Trainer;
import com.example.deukgeun.trainer.domain.model.valueobjcet.Address;
import com.example.deukgeun.trainer.domain.model.valueobjcet.Group;
import com.example.deukgeun.trainer.infrastructure.persistence.model.entity.TrainerEntity;
import com.example.deukgeun.trainer.infrastructure.persistence.model.valueobject.AddressVo;
import com.example.deukgeun.trainer.infrastructure.persistence.model.valueobject.GroupVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(
        componentModel = "spring",
        uses = {
                LicenseMapper.class,
                ProfileMapper.class,
                PostMapper.class
        },
        imports = PasswordEncoderUtil.class
)
public interface TrainerMapper {
    TrainerResponse.Info toTrainerInfoResponse(Trainer trainer);
    TrainerResponse.Detail toTrainerDetailResponse(Trainer trainer);
    @Mapping(target = "password", expression = "java(PasswordEncoderUtil.encode(request.getPassword()))")
    SaveTrainerDTO toSaveTrainerDto(String fileName, JoinRequest request);
    UpdateInfoDTO toUpdateInfoDto(UpdateInfoRequest request);
    @Mapping(target = "newPassword", expression = "java(PasswordEncoderUtil.encode(request.getNewPassword()))")
    UpdatePasswordDTO toUpdatePasswordDto(UpdatePasswordRequest request);

    @Mapping(source = "groupVo", target = "group", qualifiedByName = "toGroup")
    @Mapping(source = "addressVo", target = "address", qualifiedByName = "toAddress")
    @Mapping(source = "licenseEntities", target = "licenses", qualifiedByName = "toLicenseList", defaultExpression = "java(null)")
    @Mapping(source = "profileEntity", target = "profile", qualifiedByName = "toProfile", defaultExpression = "java(null)")
    @Mapping(source = "postEntity", target = "post", qualifiedByName = "toPost", defaultExpression = "java(null)")
    Trainer toTrainer(TrainerEntity trainerEntity);

    @Mapping(source = "group", target = "groupVo", qualifiedByName = "toGroupVo")
    @Mapping(source = "address", target = "addressVo", qualifiedByName = "toAddressVo")
    @Mapping(source = "licenses", target = "licenseEntities", qualifiedByName = "toLicenseEntityList", defaultExpression = "java(null)")
    @Mapping(source = "profile", target = "profileEntity", qualifiedByName = "toProfileEntity", defaultExpression = "java(null)")
    @Mapping(source = "post", target = "postEntity", qualifiedByName = "toPostEntity", defaultExpression = "java(null)")
    TrainerEntity toTrainerEntity(Trainer trainer);

    @Named("toGroup")
    Group toGroup(GroupVo groupVo);
    @Named("toGroupVo")
    GroupVo toGroupVo(Group group);
    @Named("toAddress")
    Address toAddress(AddressVo addressVo);
    @Named("toAddressVo")
    AddressVo toAddressVo(Address address);

}
