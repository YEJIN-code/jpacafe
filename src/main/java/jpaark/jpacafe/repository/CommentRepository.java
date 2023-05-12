package jpaark.jpacafe.repository;

import jpaark.jpacafe.domain.Comment;
import jpaark.jpacafe.domain.Member;
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

    public List<Comment> findByParentComment(Comment parentComment) {

    }


}
