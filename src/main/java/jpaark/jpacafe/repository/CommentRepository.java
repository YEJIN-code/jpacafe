package jpaark.jpacafe.repository;

import jpaark.jpacafe.domain.Comment;
import jpaark.jpacafe.domain.Member;
import jpaark.jpacafe.domain.Post;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CommentRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Comment comment){
        em.persist(comment);
    }

    public void delete(Comment comment) {
        em.remove(comment);
    }

    public Comment findOne(Long id) {
        return em.find(Comment.class, id);
    }

    public List<Comment> findByParentComment(Comment parentComment) {
        // 부모 댓글과 그 밑의 자식 댓글까지 모두 조회
        return em.createQuery("SELECT c FROM Comment c LEFT JOIN FETCH c.childComments WHERE c.parentComment IS NULL", Comment.class)
                .getResultList();
    }

    // 게시물 id와 일치하는 모든 댓글 조회
    public List<Comment> findByPostId(Long postId) {
        return em.createQuery("SELECT c FROM Comment c LEFT JOIN FETCH c.childComments WHERE c.parentComment IS NULL AND c.post.id = :postId", Comment.class)
                .setParameter("postId", postId)
                .getResultList();
    }

    // 멤버 id와 일치하는 모든 댓글 조회
    public List<Comment> findByMemberId(Long memberId) {
        return em.createQuery("SELECT c FROM Comment c WHERE c.member.id = :memberId", Comment.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

}
