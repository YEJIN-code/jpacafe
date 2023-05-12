package jpaark.jpacafe.repository;

import jpaark.jpacafe.domain.Category;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CategoryRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Category category) {
        em.persist(category);
    }

    public Category findOne(Long id) {
        return em.find(Category.class, id);
    }

    public List<Category> findAll() {
        return em.createQuery("select c from Category c", Category.class)
                .getResultList();
    }

    public List<Category> findByCafeId(Long cafeId) {
        return em.createQuery("select c from Category c where c.cafe.id = :cafeId", Category.class)
                .setParameter("cafeId", cafeId)
                .getResultList();
    }

    public List<Category> findByName(String name) {
        return em.createQuery("select c from Category c where c.name = :name", Category.class)
                .setParameter("name", name)
                .getResultList();
    }
}
