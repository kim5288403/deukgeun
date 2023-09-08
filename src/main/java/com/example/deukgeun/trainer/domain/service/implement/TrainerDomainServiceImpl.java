package com.example.deukgeun.trainer.domain.service.implement;

import com.example.deukgeun.global.util.PasswordEncoderUtil;
import com.example.deukgeun.trainer.application.dto.request.JoinRequest;
import com.example.deukgeun.trainer.application.dto.request.UpdateInfoRequest;
import com.example.deukgeun.trainer.application.dto.request.UpdatePasswordRequest;
import com.example.deukgeun.trainer.application.dto.response.LicenseResponse;
import com.example.deukgeun.trainer.domain.model.aggregate.Trainer;
import com.example.deukgeun.trainer.domain.model.entity.License;
import com.example.deukgeun.trainer.domain.model.entity.Post;
import com.example.deukgeun.trainer.domain.model.entity.Profile;
import com.example.deukgeun.trainer.domain.model.valueobjcet.Address;
import com.example.deukgeun.trainer.domain.model.valueobjcet.Group;
import com.example.deukgeun.trainer.domain.repository.TrainerRepository;
import com.example.deukgeun.trainer.domain.service.TrainerDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class TrainerDomainServiceImpl implements TrainerDomainService {

    private final TrainerRepository trainerRepository;

    /**
     * 주어진 ID를 사용하여 트레이너 정보를 삭제합니다.
     *
     * @param id 삭제할 트레이너 레코드를 식별하는 고유한 ID
     */
    @Override
    public void deleteById(Long id) {
        trainerRepository.deleteById(id);
    }

    /**
     * 이메일과 자격증 ID를 사용하여 특정 트레이너의 자격증을 삭제합니다.
     *
     * @param email      자격증을 소유한 트레이너의 이메일
     * @param licenseId  삭제할 자격증의 고유한 ID
     * @throws EntityNotFoundException 주어진 이메일 또는 자격증 ID와 일치하는 데이터를 찾을 수 없을 때 발생
     */
    @Override
    public void deleteLicenseByLicenseId(String email, Long licenseId) {
        // 주어진 이메일로 트레이너 정보를 찾습니다.
        Trainer trainer = findByEmail(email);

        // 트레이너가 소유한 자격증 목록 중에서 주어진 자격증 ID와 일치하는 자격증을 찾습니다.
        License license = trainer.getLicenses()
                .stream()
                .filter(item -> item.getId().equals(licenseId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("자격증을 찾을 수 없습니다."));

        // 트레이너의 자격증 목록에서 찾은 자격증을 삭제합니다.
        trainer.getLicenses().remove(license);

        // 변경된 트레이너 정보를 데이터베이스에 저장합니다.
        trainerRepository.save(trainer);
    }

    /**
     * 특정 트레이너가 작성한 게시물을 삭제합니다.
     *
     * @param email 게시물을 작성한 트레이너의 이메일
     */
    @Override
    public void deletePost(String email) {
        // 주어진 이메일로 트레이너 정보를 찾습니다.
        Trainer trainer = findByEmail(email);

        // 트레이너의 게시물을 삭제합니다.
        trainer.deletePost();

        // 변경된 트레이너 정보를 데이터베이스에 저장합니다.
        trainerRepository.save(trainer);
    }

    /**
     * 주어진 이메일 주소에 해당하는 트레이너가 존재하는지 확인합니다.
     *
     * @param email 확인할 이메일 주소
     * @return 해당 이메일 주소에 대한 트레이너가 존재하면 true, 그렇지 않으면 false 반환
     */
    @Override
    public boolean existsByEmail(String email) {
        return trainerRepository.existsByEmail(email);
    }

    /**
     * 주어진 고유한 ID에 해당하는 트레이너 정보를 조회합니다.
     *
     * @param id 조회할 트레이너의 고유한 ID
     * @return 고유한 ID에 해당하는 트레이너 객체
     * @throws EntityNotFoundException 주어진 ID에 해당하는 트레이너를 찾을 수 없을 때 발생
     */
    @Override
    public Trainer findById(Long id) throws EntityNotFoundException {
        return trainerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
    }

    /**
     * 주어진 이메일 주소에 해당하는 트레이너 정보를 조회합니다.
     *
     * @param email 조회할 트레이너의 이메일 주소
     * @return 이메일 주소에 해당하는 트레이너 객체
     * @throws EntityNotFoundException 주어진 이메일 주소에 해당하는 트레이너를 찾을 수 없을 때 발생
     */
    @Override
    public Trainer findByEmail(String email) throws EntityNotFoundException {
        return trainerRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
    }

    /**
     * 주어진 이메일 주소에 해당하는 트레이너의 사용자 정보를 로드합니다.
     *
     * @param email 로드할 트레이너의 이메일 주소
     * @return 이메일 주소에 해당하는 트레이너의 사용자 정보
     * @throws UsernameNotFoundException 주어진 이메일 주소에 해당하는 사용자를 찾을 수 없을 때 발생
     */
    @Override
    public UserDetails loadUserByTrainerUsername(String email) throws UsernameNotFoundException {
        return trainerRepository.loadUserByUsername(email);
    }

    /**
     * 회원가입 요청을 처리하고, 새로운 트레이너 정보를 저장합니다.
     *
     * @param request   회원가입 요청 정보
     * @param fileName  프로필 이미지 파일 이름
     * @return 저장된 트레이너 정보
     */
    @Override
    public Trainer save(JoinRequest request, String fileName) {
        // 그룹 정보를 생성합니다.
        Group group = new Group(
                request.getGroupStatus(),
                request.getGroupName()
        );

        // 주소 정보를 생성합니다.
        Address address =  new Address(
                request.getPostcode(),
                request.getJibunAddress(),
                request.getRoadAddress(),
                request.getDetailAddress(),
                request.getExtraAddress()
        );

        // 새로운 트레이너 정보를 생성합니다.
        Trainer trainer = Trainer.create(
                request.getName(),
                request.getEmail(),
                PasswordEncoderUtil.encode(request.getPassword()),
                group,
                address,
                request.getGender(),
                request.getPrice(),
                request.getIntroduction()
        );

        // 프로필 정보를 생성하고 트레이너에 연결합니다.
        Profile profile = Profile.create(fileName);
        trainer.setProfile(profile);

        // 생성된 트레이너 정보를 저장하고 반환합니다.
        return trainerRepository.save(trainer);
    }

    /**
     * 트레이너의 자격증 정보를 저장하고, 트레이너 정보를 업데이트합니다.
     *
     * @param email         자격증을 추가할 트레이너의 이메일 주소
     * @param licenseResult 자격증 정보
     * @return 업데이트된 트레이너 정보
     */
    @Override
    public Trainer saveLicense(String email, LicenseResponse.Result licenseResult) {
        // 주어진 이메일로 트레이너 정보를 찾습니다.
        Trainer trainer = findByEmail(email);

        // 자격증 정보를 생성합니다.
        License license = License.create(licenseResult.getCertificatename(), licenseResult.getNo(), trainer.getId());

        // 트레이너의 자격증 목록에 생성한 자격증을 추가합니다.
        trainer.getLicenses().add(license);

        // 업데이트된 트레이너 정보를 저장하고 반환합니다.
        return trainerRepository.save(trainer);
    }

    /**
     * 주어진 이메일 주소에 해당하는 트레이너 정보를 업데이트합니다.
     *
     * @param request 업데이트할 정보를 담은 요청 객체
     */
    @Override
    public void updateInfoByEmail(UpdateInfoRequest request) {
        // 주어진 이메일로 트레이너 정보를 찾습니다.
        Trainer trainer = findByEmail(request.getEmail());

        // 트레이너 정보를 주어진 요청 객체를 사용하여 업데이트합니다.
        trainer.updateInfo(request);

        // 변경된 트레이너 정보를 저장합니다.
        trainerRepository.save(trainer);
    }

    /**
     * 주어진 이메일 주소에 해당하는 트레이너의 프로필 이미지 경로를 업데이트하고, 이전 경로를 반환합니다.
     *
     * @param email 주소에 해당하는 트레이너의 이메일
     * @param path  새로운 프로필 이미지 경로
     * @return 이전 프로필 이미지 경로
     */
    @Override
    public String updateProfileByEmail(String email, String path) {
        // 주어진 이메일로 트레이너 정보를 찾습니다.
        Trainer trainer = findByEmail(email);

        // 이전 프로필 이미지 경로를 저장합니다.
        String existingPath = trainer.getProfile().getPath();

        // 새로운 프로필 이미지 경로를 업데이트합니다.
        trainer.getProfile().updatePath(path);

        // 변경된 트레이너 정보를 저장합니다.
        trainerRepository.save(trainer);

        // 이전 프로필 이미지 경로를 반환합니다.
        return existingPath;
    }


    /**
     * 주어진 이메일 주소에 해당하는 트레이너의 비밀번호를 업데이트합니다.
     *
     * @param request 비밀번호 업데이트 요청 정보
     */
    @Override
    public void updatePasswordByEmail(UpdatePasswordRequest request) {
        // 새로운 비밀번호를 암호화합니다.
        String newPassword = PasswordEncoderUtil.encode(request.getNewPassword());

        // 주어진 이메일로 트레이너 정보를 찾습니다.
        Trainer foundTrainer = findByEmail(request.getEmail());

        // 트레이너의 비밀번호를 업데이트합니다.
        foundTrainer.updatePassword(newPassword);

        // 변경된 트레이너 정보를 저장합니다.
        trainerRepository.save(foundTrainer);
    }

    /**
     * 주어진 이메일 주소에 해당하는 트레이너의 게시물을 업로드하거나 업데이트합니다.
     *
     * @param email 주소에 해당하는 트레이너의 이메일
     * @param html  게시물의 HTML 내용
     */
    @Override
    public void uploadPostByEmail(String email, String html) {
        // 주어진 이메일로 트레이너 정보를 찾습니다.
        Trainer trainer = findByEmail(email);

        // 트레이너가 이미 게시물을 가지고 있는지 확인합니다.
        if (trainer.doesPostExist()) {
            // 트레이너가 게시물을 가지고 있으면 HTML 내용을 업데이트합니다.
            trainer.getPost().updateHtml(html);
        } else {
            // 트레이너가 게시물을 가지고 있지 않으면 새로운 게시물을 생성합니다.
            Post post = Post.create(html);
            trainer.setPost(post);
        }

        // 변경된 트레이너 정보를 저장합니다.
        trainerRepository.save(trainer);
    }
}
