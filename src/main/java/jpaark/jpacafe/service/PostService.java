package jpaark.jpacafe.service;

import jpaark.jpacafe.controller.form.PostForm;
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
    private final CategoryService categoryService;

    @Transactional(readOnly = false)
    public Long join(Post post) {
        postRepository.save(post);
        return post.getId();
    }

    @Transactional
    public Post updatePost(Long postId, String title, String content, String category) {
        Post findPost = postRepository.findOne(postId);
        findPost.setTitle(title);
        findPost.setContent(content);
        findPost.setCategory(categoryService.findByName(category).get(0));

        return findPost;
    }

    @Transactional
    public void deletePost(Long postId) {
        Post post = postRepository.findOne(postId);
        if (post != null) {
            postRepository.delete(post);
        }
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

    public List<Post> findByTitle(String keyword) {
        return postRepository.searchPostByTitle(keyword);
    }

    public List<Post> findByContent(String keyword) {
        return postRepository.searchPostByContent(keyword);
    }

    public List<Post> findByAllKeyword(String keyword) {
        return postRepository.searchPostByAll(keyword);
    }

    public int newPostCountCal(Long categoryId) {
        return postRepository.newPostCountCal(categoryId);
    }
}
