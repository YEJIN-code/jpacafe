package jpaark.jpacafe.repository;

import jpaark.jpacafe.domain.Grade;
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
}
