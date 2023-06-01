package jpaark.jpacafe.repository;

import jpaark.jpacafe.domain.Member;
import jpaark.jpacafe.domain.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository // 스프링 빈으로 등록
@RequiredArgsConstructor
public class UserRepository {

    private static Map<String, Users> store = new HashMap<>(); //static 사용
    private final EntityManager em;

    public void save(Users user) {
        store.put(user.getId(), user);
        em.persist(user);
    }

    public Users findOne(String id) {
        return em.find(Users.class, id);
    }

    public List<Users> findAll() {
        return em.createQuery("select u from Users u", Users.class)
                .getResultList();
    }

    public List<Users> findById(String id) {
        return em.createQuery("select u from Users u where u.id = :id", Users.class)
                .setParameter("id", id)
                .getResultList();
    }

    public List<Member> findAllMember(String id) {
        String jpql = "SELECT m FROM Users u JOIN u.members m WHERE u.id = :userId";

        List<Member> members = em.createQuery(jpql, Member.class)
                .setParameter("userId", id)
                .getResultList();

        return members;
    }

}
