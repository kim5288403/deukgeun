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
import com.example.deukgeun.trainer.infrastructure.persistence.repository.TrainerJpaRepository;
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

    private final TrainerJpaRepository trainerRepository;

    /**
     * 주어진 고유한 ID에 해당하는 트레이너 데이터를 데이터베이스에서 삭제합니다.
     *
     * @param id 삭제할 트레이너 데이터의 고유한 ID
     */
    @Override
    public void deleteById(Long id) {
        trainerRepository.deleteById(id);
    }

    /**
     * 주어진 이메일 주소가 데이터베이스에 존재하는지 여부를 확인합니다.
     *
     * @param email 확인할 이메일 주소
     * @return 해당 이메일 주소가 데이터베이스에 존재하면 true, 그렇지 않으면 false 반환
     */
    @Override
    public boolean existsByEmail(String email) {
        return trainerRepository.existsByEmail(email);
    }

    /**
     * 주어진 고유한 ID에 해당하는 트레이너 정보를 조회하고 반환합니다.
     *
     * @param id 조회할 트레이너의 고유한 ID
     * @return 고유한 ID에 해당하는 트레이너 정보를 포함하는 Optional 객체
     */
    @Override
    public Optional<Trainer> findById(Long id) {
        // trainerRepository를 사용하여 주어진 ID에 해당하는 트레이너 엔티티를 조회합니다.
        Optional<TrainerEntity> trainerEntity = trainerRepository.findById(id);

        // 조회한 트레이너 엔티티를 변환하여 Trainer 객체로 반환합니다.
        return trainerEntity.map(this::convert);
    }

    /**
     * 주어진 이메일 주소에 해당하는 트레이너 정보를 조회하고 반환합니다.
     *
     * @param email 조회할 트레이너의 이메일 주소
     * @return 이메일 주소에 해당하는 트레이너 정보를 포함하는 Optional 객체
     */
    @Override
    public Optional<Trainer> findByEmail(String email) {
        // trainerRepository를 사용하여 주어진 이메일 주소에 해당하는 트레이너 엔티티를 조회합니다.
        Optional<TrainerEntity> trainerEntity = trainerRepository.findByEmail(email);

        // 조회한 트레이너 엔티티를 변환하여 Trainer 객체로 반환합니다.
        return trainerEntity.map(this::convert);
    }

    /**
     * 주어진 이메일 주소에 해당하는 트레이너의 사용자 정보를 로드합니다.
     *
     * @param email 로드할 트레이너의 이메일 주소
     * @return 이메일 주소에 해당하는 트레이너의 사용자 정보
     * @throws UsernameNotFoundException 주어진 이메일 주소에 해당하는 사용자를 찾을 수 없을 때 발생
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // trainerRepository를 사용하여 주어진 이메일 주소에 해당하는 트레이너 엔티티를 조회합니다.
        return trainerRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }

    /**
     * 트레이너 정보를 저장하고 저장된 트레이너 정보를 반환합니다.
     *
     * @param trainer 저장할 트레이너 정보
     * @return 저장된 트레이너 정보
     */
    @Override
    public Trainer save(Trainer trainer) {
        // 트레이너 정보를 엔티티로 변환하여 저장합니다.
        TrainerEntity saveTrainerEntity = trainerRepository.save(convert(trainer));

        // 저장된 트레이너 엔티티를 다시 트레이너 객체로 변환하여 반환합니다.
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
                .profileId(trainer.getProfile().getId())
                .postEntity(covert(trainer.getPost()))
                .postId(setPostId(trainer.getPost()))
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
