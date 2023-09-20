package com.example.deukgeun.trainer.infrastructure.persistence.adapter;

import com.example.deukgeun.trainer.domain.model.aggregate.Trainer;
import com.example.deukgeun.trainer.domain.repository.TrainerRepository;
import com.example.deukgeun.trainer.infrastructure.persistence.mapper.TrainerMapper;
import com.example.deukgeun.trainer.infrastructure.persistence.model.entity.TrainerEntity;
import com.example.deukgeun.trainer.infrastructure.persistence.repository.TrainerJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TrainerRepositoryAdapter implements TrainerRepository {
    private final TrainerJpaRepository trainerRepository;
    private final TrainerMapper trainerMapper;

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
        return trainerEntity.map(trainerMapper::toTrainer);
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
        return trainerEntity.map(trainerMapper::toTrainer);
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
        TrainerEntity saveTrainerEntity = trainerRepository.save(trainerMapper.toTrainerEntity(trainer));

        // 저장된 트레이너 엔티티를 다시 트레이너 객체로 변환하여 반환합니다.
        return trainerMapper.toTrainer(saveTrainerEntity);
    }
}
