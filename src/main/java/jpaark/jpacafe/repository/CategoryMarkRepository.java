package jpaark.jpacafe.repository;

import jpaark.jpacafe.domain.*;
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

    public void delete(CategoryMark categoryMark) {
        em.remove(categoryMark);
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

    public long countByUserId(String userId) {
        return em.createQuery("select count(c) from CategoryMark c where c.user.id = :id", Long.class)
                .setParameter("id", userId)
                .getSingleResult();
    }

    public List<CategoryMark> findByUserIdAndCategoryId(String userId, Long categoryId) {
        return em.createQuery("select c from CategoryMark c where c.user.id = :userId and c.category.id = :categoryId", CategoryMark.class)
                .setParameter("userId", userId)
                .setParameter("categoryId", categoryId)
                .getResultList();
    }


}
