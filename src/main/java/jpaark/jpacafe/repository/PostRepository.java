package jpaark.jpacafe.repository;

import jpaark.jpacafe.domain.Post;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
}
