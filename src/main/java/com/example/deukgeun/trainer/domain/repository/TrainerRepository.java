package com.example.deukgeun.trainer.domain.repository;

import com.example.deukgeun.trainer.domain.model.aggregate.Trainer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public interface TrainerRepository {
    void deleteById(Long id);
    boolean existsByEmail(String email);
    Optional<Trainer> findById(Long id);
    Optional<Trainer> findByEmail(String email);
    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;
    Trainer save(Trainer trainer);

}
