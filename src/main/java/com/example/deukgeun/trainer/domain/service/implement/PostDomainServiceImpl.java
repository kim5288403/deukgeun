package com.example.deukgeun.trainer.domain.service.implement;

import com.example.deukgeun.trainer.domain.model.entity.Post;
import com.example.deukgeun.trainer.domain.repository.PostRepository;
import com.example.deukgeun.trainer.domain.service.PostDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class PostDomainServiceImpl implements PostDomainService {
    private final PostRepository postRepository;

    @Value("${trainer.post.filePath}")
    private String postFilePath;

    @Value("${trainer.post.url}")
    private String postUrl;
    @Override
    public void deleteById(Long id) {
        postRepository.deleteById(id);
    }

    @Override
    public boolean existsByTrainerId(Long trainerId) {
        return postRepository.existsByTrainerId(trainerId);
    }

    @Override
    public Post findByTrainerId(Long id) throws EntityNotFoundException {
        return postRepository.findByTrainerId(id).orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
    }
    @Override
    public Post save(String html, Long trainerId) {
        Post post = Post.create(html, trainerId);
        return postRepository.save(post);
    }

    @Override
    public Post update(Post post) {
        return postRepository.save(post);
    }

}
