package jpaark.jpacafe.service;

import jpaark.jpacafe.domain.Category;
import jpaark.jpacafe.domain.Post;
import jpaark.jpacafe.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor // final 로 된 걸 생성해줌
public class PostService {

    private final PostRepository postRepository;

    @Transactional(readOnly = false)
    public Long join(Post post) {
        postRepository.save(post);
        return post.getId();
    }

    public Post findOne(Long id) {
        return postRepository.findOne(id);
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public List<Post> findByCafeId(Long id) {
        return postRepository.findByCafeId(id);
    }

    public List<Post> findByCategoryId(Long id) {
        return postRepository.findByCategoryId(id);
    }

    public List<Post> findLatestPosts(int count) {
        return postRepository.findLatestPosts(3);
    }
}
