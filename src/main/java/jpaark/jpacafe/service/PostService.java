package jpaark.jpacafe.service;

import jpaark.jpacafe.controller.form.PostForm;
import jpaark.jpacafe.domain.Category;
import jpaark.jpacafe.domain.Post;
import jpaark.jpacafe.repository.CafeRepository;
import jpaark.jpacafe.repository.CategoryRepository;
import jpaark.jpacafe.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor // final 로 된 걸 생성해줌
public class PostService {

    private final PostRepository postRepository;
    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = false)
    public Long join(Post post) {
        postRepository.save(post);
        Category category = categoryRepository.findOne(post.getCategory().getId());
        category.setTotalPlus();
        log.info("update category = {}, total = {}", category.getId(), category.getTotal());
        return post.getId();
    }

    @Transactional
    public Post updatePostView(Long postId) {
        Post findPost = postRepository.findOne(postId);
        findPost.setViewPlus();

        return findPost;
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
    public void withdrawalPost(Long postId) {
        Post findPost = postRepository.findOne(postId);
        findPost.setWriter("(알수없음)");
    }

    @Transactional
    public void deletePost(Long postId) {
        Post post = postRepository.findOne(postId);
        if (post != null) {
            postRepository.delete(post);
            Category category = categoryRepository.findOne(post.getCategory().getId());
            category.setTotalMinus();
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

    public List<Post> findByUserId(String userId) {
        return postRepository.findByUserId(userId);
    }

    public int totalPostInCategory(Long categoryId) {
        return postRepository.totalPostsInCategory(categoryId);
    }

    public int newPostCountCal(Long categoryId) {
        return postRepository.newPostCountCal(categoryId);
    }
}
