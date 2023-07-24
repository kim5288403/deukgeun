package com.example.deukgeun.trainer.infrastructure.persistence.adapter;

import com.example.deukgeun.trainer.domain.model.entity.Post;
import com.example.deukgeun.trainer.domain.repository.PostRepository;
import com.example.deukgeun.trainer.infrastructure.persistence.entity.PostEntity;
import com.example.deukgeun.trainer.infrastructure.persistence.repository.PostRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PostRepositoryAdapter implements PostRepository {
    private final PostRepositoryImpl postRepository;

    @Override
    public void deleteById(Long id) {
        postRepository.deleteById(id);
    }

    @Override
    public boolean existsByTrainerId(Long trainerId) {
        return postRepository.existsByTrainerId(trainerId);
    }

    @Override
    public Optional<Post> findByTrainerId(Long id) {
        Optional<PostEntity> postEntity = postRepository.findByTrainerId(id);
        return postEntity.map(this::covert);
    }

    @Override
    public Post save(Post post) {
        PostEntity postEntity = postRepository.save(covert(post));
        return covert(postEntity);
    }

    private Post covert(PostEntity postEntity) {
        return new Post
                (
                        postEntity.getId(),
                        postEntity.getHtml(),
                        postEntity.getTrainerId()
                );
    }

    private PostEntity covert(Post post) {
        return PostEntity
                .builder()
                .id(post.getId())
                .html(post.getHtml())
                .trainerId(post.getTrainerId())
                .build();
    }
}
