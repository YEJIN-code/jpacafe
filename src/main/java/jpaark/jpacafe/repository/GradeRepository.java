package jpaark.jpacafe.repository;

import jpaark.jpacafe.domain.Grade;
import jpaark.jpacafe.domain.Member;
import jpaark.jpacafe.domain.Status.StatusSet;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class GradeRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Grade grade) {
        em.persist(grade);
    }

    public Grade findOne(Long id) {
        return em.find(Grade.class, id);
    }

    // 카페의 등급 검색
    public List<Grade> findByCafeId(Long cafeId) {
        return em.createQuery("select g from Grade g where g.cafe.id = :cafeId", Grade.class)
                .setParameter("cafeId", cafeId)
                .getResultList();
    }

    // 등급명 검색
    public List<Grade> findByName(String name) {
        return em.createQuery("select g from Grade g where g.name = :name", Grade.class)
                .setParameter("name", name)
                .getResultList();
    }

    public List<Grade> findByMemberId(Long memberId) {
        return em.createQuery("SELECT g FROM Grade g JOIN g.members m WHERE m.id = :memberId", Grade.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }


    public List<Grade> findNormalGradesByCafeId(Long cafeId) {

        String jpql = "SELECT g FROM Grade g " +
                "WHERE g.cafe.id = :cafeId AND " +
                "g.postPermission = 0 AND " +
                "g.categoryPermission = 0 AND " +
                "g.cafePermission = 0";

        List<Grade> grades = em.createQuery(jpql, Grade.class)
                .setParameter("cafeId", cafeId)
                .getResultList();

        return grades;
    }


}
