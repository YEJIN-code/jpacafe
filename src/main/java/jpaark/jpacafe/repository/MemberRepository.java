package jpaark.jpacafe.repository;

import jpaark.jpacafe.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Slf4j
public class MemberRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Member member){
        em.persist(member);
    }

    public Member findByNickname(String nickname) {
        return em.find(Member.class, nickname);
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll(Long cafe_id) { // 카페 내 모든 멤버 검색
        return em.createQuery("select m from Member m where m.cafe.id = :cafe_id", Member.class)
                .setParameter("cafe_id", cafe_id)
                .getResultList();
    }

    public List<Member> findListByNickname(String nickname) {
        return em.createQuery("select m from Member m where m.nickname = :nickname", Member.class)
                .setParameter("nickname", nickname)
                .getResultList();
    }

    public List<Member> findByCafeIdAndNickname(Long cafeId, String nickname) {
        return em.createQuery("select m from Member m where m.cafe.id = :cafeId and m.nickname = :nickname", Member.class)
                .setParameter("cafeId", cafeId)
                .setParameter("nickname", nickname)
                .getResultList();
    }

    public List<Member> findByCafeIdAndUserId(Long cafeId, String userId) {
        return em.createQuery("SELECT m FROM Member m JOIN m.user u JOIN m.cafe c WHERE u.id = :userId AND c.id = :cafeId", Member.class)
                .setParameter("userId", userId)
                .setParameter("cafeId", cafeId)
                .getResultList();

    }



}
