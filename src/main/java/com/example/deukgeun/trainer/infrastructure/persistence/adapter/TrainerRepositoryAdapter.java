package com.example.deukgeun.trainer.infrastructure.persistence.adapter;

import com.example.deukgeun.trainer.domain.model.aggregate.Trainer;
import com.example.deukgeun.trainer.domain.model.entity.License;
import com.example.deukgeun.trainer.domain.model.entity.Post;
import com.example.deukgeun.trainer.domain.model.entity.Profile;
import com.example.deukgeun.trainer.domain.model.valueobjcet.Address;
import com.example.deukgeun.trainer.domain.model.valueobjcet.Group;
import com.example.deukgeun.trainer.domain.repository.TrainerRepository;
import com.example.deukgeun.trainer.infrastructure.persistence.model.entity.LicenseEntity;
import com.example.deukgeun.trainer.infrastructure.persistence.model.entity.PostEntity;
import com.example.deukgeun.trainer.infrastructure.persistence.model.entity.ProfileEntity;
import com.example.deukgeun.trainer.infrastructure.persistence.model.entity.TrainerEntity;
import com.example.deukgeun.trainer.infrastructure.persistence.model.valueobject.AddressVo;
import com.example.deukgeun.trainer.infrastructure.persistence.model.valueobject.GroupVo;
import com.example.deukgeun.trainer.infrastructure.persistence.repository.TrainerRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TrainerRepositoryAdapter implements TrainerRepository {

    private final TrainerRepositoryImpl trainerRepository;

    @Override
    public void deleteById(Long id) {
        trainerRepository.deleteById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return trainerRepository.existsByEmail(email);
    }

    @Override
    public Optional<Trainer> findById(Long id) {
        Optional<TrainerEntity> trainerEntity = trainerRepository.findById(id);
        return trainerEntity.map(this::convert);
    }

    @Override
    public Optional<Trainer> findByEmail(String email) {
        Optional<TrainerEntity> trainerEntity = trainerRepository.findByEmail(email);
        return trainerEntity.map(this::convert);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return trainerRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을수 없습니다."));
    }

    @Override
    public Trainer save(Trainer trainer) {
        TrainerEntity saveTrainerEntity = trainerRepository.save(convert(trainer));
        return convert(saveTrainerEntity);
    }

    private TrainerEntity convert(Trainer trainer) {
        return TrainerEntity
                .builder()
                .id(trainer.getId())
                .name(trainer.getName())
                .email(trainer.getEmail())
                .password(trainer.getPassword())
                .groupVo(new GroupVo(
                        trainer.getGroup().getGroupStatus(),
                        trainer.getGroup().getGroupName()
                        ))
                .addressVo(covert(trainer.getAddress()))
                .gender(trainer.getGender())
                .price(trainer.getPrice())
                .introduction(trainer.getIntroduction())
                .licenseEntities(trainer.getLicenses().stream().map(this::convert).collect(Collectors.toList()))
                .profileEntity(convert(trainer.getProfile()))
                .profile_id(trainer.getProfile().getId())
                .postEntity(covert(trainer.getPost()))
                .post_id(setPostId(trainer.getPost()))
                .build();
    }

    private Trainer convert(TrainerEntity trainerEntity) {
        return new Trainer(
                trainerEntity.getId(),
                trainerEntity.getName(),
                trainerEntity.getEmail(),
                trainerEntity.getPassword(),
                new Group(
                        trainerEntity.getGroupVo().getGroupStatus(),
                        trainerEntity.getGroupVo().getGroupName()
                ),
                covert(trainerEntity.getAddressVo()),
                trainerEntity.getGender(),
                trainerEntity.getPrice(),
                trainerEntity.getIntroduction(),
                trainerEntity.getLicenseEntities().stream().map(this::convert).collect(Collectors.toList()),
                convert(trainerEntity.getProfileEntity()),
                covert(trainerEntity.getPostEntity())
        );
    }

    private LicenseEntity convert(License license) {
        return LicenseEntity
                .builder()
                .id(license.getId())
                .certificateName(license.getCertificateName())
                .licenseNumber(license.getLicenseNumber())
                .trainerId(license.getTrainerId())
                .build();
    }

    private License convert(LicenseEntity licenseEntity) {
        return new License(
                licenseEntity.getId(),
                licenseEntity.getCertificateName(),
                licenseEntity.getLicenseNumber(),
                licenseEntity.getTrainerId(),
                licenseEntity.getCreatedDate()
        );
    }

    private Profile convert(ProfileEntity profileEntity) {
        return new Profile
                (
                        profileEntity.getId(),
                        profileEntity.getPath()
                );
    }

    private ProfileEntity convert(Profile profile) {
        return ProfileEntity
                .builder()
                .id(profile.getId())
                .path(profile.getPath())
                .build();
    }

    private Post covert(PostEntity postEntity) {
        if (postEntity == null) {
            return null;
        }
        return new Post
                (
                        postEntity.getId(),
                        HtmlUtils.htmlUnescape(postEntity.getHtml())
                );
    }

    private PostEntity covert(Post post) {
        if (post == null) {
            return null;
        }
        return PostEntity
                .builder()
                .id(post.getId())
                .html(post.getHtml())
                .build();
    }

    private Long setPostId(Post post) {
        if (post == null) {
            return null;
        }
        return post.getId();
    }

    private AddressVo covert(Address address) {
        return new AddressVo(
                address.getPostcode(),
                address.getJibunAddress(),
                address.getRoadAddress(),
                address.getDetailAddress(),
                address.getExtraAddress()
        );
    }

    private Address covert(AddressVo addressVo) {
        return new Address(
                addressVo.getPostcode(),
                addressVo.getJibunAddress(),
                addressVo.getRoadAddress(),
                addressVo.getDetailAddress(),
                addressVo.getExtraAddress()
        );
    }
}
