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
import com.example.deukgeun.trainer.domain.repository.TrainerRepository;
import com.example.deukgeun.trainer.domain.service.TrainerDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class TrainerDomainServiceImpl implements TrainerDomainService {

    private final TrainerRepository trainerRepository;

    @Override
    public void deleteById(Long id) {
        trainerRepository.deleteById(id);
    }

    @Override
    public void deleteLicenseByLicenseId(String email, Long licenseId) {
        Trainer trainer = findByEmail(email);

        License license = trainer.getLicenses()
                .stream()
                .filter(item -> item.getId().equals(licenseId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("자격증을 찾을 수 없습니다."));

        trainer.getLicenses().remove(license);

        trainerRepository.save(trainer);
    }

    @Override
    public void deletePost(String email) {
        Trainer trainer = findByEmail(email);
        Post post = trainer.getPost();
        post.delete();

        trainerRepository.save(trainer);
    }

    @Override
    public boolean existsByEmail(String email) {
        return trainerRepository.existsByEmail(email);
    }

    @Override
    public Trainer findById(Long id) throws EntityNotFoundException {
        return trainerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
    }

    @Override
    public Trainer findByEmail(String email) throws EntityNotFoundException {
        return trainerRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
    }

    @Override
    public Trainer save(JoinRequest request, String fileName) {
        Trainer trainer = Trainer.create(
                request.getName(),
                request.getEmail(),
                PasswordEncoderUtil.encode(request.getPassword()),
                request.getGroupStatus(),
                request.getGroupName(),
                new Address(
                        request.getPostcode(),
                        request.getJibunAddress(),
                        request.getRoadAddress(),
                        request.getDetailAddress(),
                        request.getExtraAddress()
                ),
                request.getGender(),
                request.getPrice(),
                request.getIntroduction()
        );

        Profile profile = Profile.create(trainer.getId(), fileName);

        trainer.setProfile(profile);

        return trainerRepository.save(trainer);
    }

    @Override
    public Trainer saveLicense(String email, LicenseResponse.Result licenseResult) {
        Trainer trainer = findByEmail(email);
        License license = License.create(licenseResult.getCertificatename(), licenseResult.getNo(), trainer.getId());
        trainer.getLicenses().add(license);

        return trainerRepository.save(trainer);
    }

    @Override
    public void updateInfo(UpdateInfoRequest request) {
        Trainer trainer = findByEmail(request.getEmail());

        trainer.updateInfo(request);
        System.out.println(trainer.getAddress().getDetailAddress());

        trainerRepository.save(trainer);
    }

    @Override
    public void updateProfile(Trainer trainer, String path) {
        trainer.getProfile().updatePath(path);

        trainerRepository.save(trainer);
    }

    @Override
    public void updatePassword(UpdatePasswordRequest request) {
        String newPassword = PasswordEncoderUtil.encode(request.getNewPassword());
        Trainer foundTrainer = findByEmail(request.getEmail());

        foundTrainer.updatePassword(newPassword);
        trainerRepository.save(foundTrainer);
    }

    @Override
    public Trainer uploadPost(String email, String html) {
        Trainer trainer = findByEmail(email);

        if (trainer.doesPostExist()) {
            trainer.getPost().updateHtml(html);
        } else {
            Post post = Post.create(html, trainer.getId());
            trainer.setPost(post);
        }

        return trainerRepository.save(trainer);
    }

}
