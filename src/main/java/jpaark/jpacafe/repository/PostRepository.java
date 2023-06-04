package jpaark.jpacafe.repository;

import jpaark.jpacafe.domain.Cafe;
import jpaark.jpacafe.domain.CategoryMark;
import jpaark.jpacafe.domain.Post;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;

@Repository
public class PostRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Post post) {
        em.persist(post);
    }

    // 기본키로 찾기
    public Post findOne(Long id) {
        return em.find(Post.class, id);
    }


    // 전체 포스트 찾기
    public List<Post> findAll() {
        return em.createQuery("select p from Post p", Post.class)
                .getResultList();
    }

    // 카페 id로 전체 포스트 찾기
    public List<Post> findByCafeId(Long id) {
        return em.createQuery("select p from Post p where p.category.cafe.id = :id", Post.class)
                .setParameter("id", id)
                .getResultList();
    }

    // 게시판 id로 전체 포스트 찾기
    public List<Post> findByCategoryId(Long id) {
        return em.createQuery("select p from Post p where p.category.id = :id", Post.class)
                .setParameter("id", id)
                .getResultList();
    }

    // 최신 포스트 가져오기
    public List<Post> findLatestPosts(int count) {
        return em.createQuery("select p from Post p order by p.dateTime desc", Post.class)
                .setMaxResults(count)
                .getResultList();
    }

    public void delete(Post post) {
        em.remove(post);
    }

    public void deleteById(Long id) {
        Post post = em.find(Post.class, id);
        if (post != null) {
            em.remove(post);
        }
    }

    public List<Post> searchPostByTitle(String keyword) {
        return em.createQuery("SELECT p FROM Post p WHERE p.title LIKE CONCAT('%', :keyword, '%')", Post.class)
                .setParameter("keyword", keyword)
                .getResultList();
    }

    public List<Post> searchPostByContent(String keyword) {
        return em.createQuery("SELECT p FROM Post p WHERE p.content LIKE CONCAT('%', :keyword, '%')", Post.class)
                .setParameter("keyword", keyword)
                .getResultList();
    }

    public List<Post> searchPostByAll(String keyword) {
        return em.createQuery("SELECT p FROM Post p WHERE p.title LIKE CONCAT('%', :keyword, '%') OR p.content LIKE CONCAT('%', :keyword, '%')", Post.class)
                .setParameter("keyword", keyword)
                .getResultList();
    }

    public int newPostCountCal(Long categoryId) {
        LocalDate today = LocalDate.now();

        return em.createQuery("SELECT COUNT(p) FROM Post p WHERE p.category.id = :category AND FUNCTION('TRUNC', p.dateTime) = :today", Long.class)
                .setParameter("category", categoryId)
                .setParameter("today", today)
                .getSingleResult()
                .intValue();
    }

}
