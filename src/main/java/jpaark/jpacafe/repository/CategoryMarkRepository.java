package jpaark.jpacafe.repository;

import jpaark.jpacafe.domain.Cafe;
import jpaark.jpacafe.domain.Category;
import jpaark.jpacafe.domain.CategoryMark;
import jpaark.jpacafe.domain.Users;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;

@Repository
public class CategoryMarkRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(CategoryMark categoryMark){
        em.persist(categoryMark);
    }

    // 기본키로 찾기
    public CategoryMark findOne(Long id) {
        return em.find(CategoryMark.class, id);
    }

    public List<CategoryMark> findById(Long id) {
        return em.createQuery("select c from CategoryMark c where c.id = :id", CategoryMark.class)
                .setParameter("id", id)
                .getResultList();
    }

    public List<CategoryMark> findByUserId(String userId) {
        return em.createQuery("select c from CategoryMark c where c.user.id = :id", CategoryMark.class)
                .setParameter("id", userId)
                .getResultList();
    }



}
