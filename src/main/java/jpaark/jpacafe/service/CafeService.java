package jpaark.jpacafe.service;

import jpaark.jpacafe.domain.Cafe;
import jpaark.jpacafe.domain.Grade;
import jpaark.jpacafe.domain.Member;
import jpaark.jpacafe.domain.Status.StatusSet;
import jpaark.jpacafe.domain.Users;
import jpaark.jpacafe.repository.CafeRepository;
import jpaark.jpacafe.repository.GradeRepository;
import jpaark.jpacafe.repository.MemberRepository;
import jpaark.jpacafe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // 조회 성능 최적화
@RequiredArgsConstructor // final 로 된 걸 생성해줌
public class CafeService {

    private final CafeRepository cafeRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final UserRepository userRepository;
    private final GradeRepository gradeRepository;

    // 카페 생성
    @Transactional(readOnly = false)
    public Long join(Cafe cafe) {
        validateDuplicateCafe(cafe);
        cafeRepository.save(cafe);

        return cafe.getId();
    }

    // 중복 카페 검증
    public void validateDuplicateCafe(Cafe cafe) {
        List<Cafe> cafeList = cafeRepository.findListByName(cafe.getName());
        if (!cafeList.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 카페명입니다.");
        }
    }

    // 카페 전체 조회
    public List<Cafe> findAll(Long cafeId) {
        return cafeRepository.findAll();
    }

    public Cafe findOne(Long cafeId) {
        return cafeRepository.findOne(cafeId);
    }

    // 카페 이름으로 조회
    public List<Cafe> findByName(String name) {
        return cafeRepository.findListByName(name);
    }

    @Transactional(readOnly = false)
    public Member createCafe(String userId, Long cafeId, String nickname) {
        Users user = userRepository.findOne(userId);
        Cafe cafe = cafeRepository.findOne(cafeId);

        Grade grade = new Grade();
        grade.setCafePermission(StatusSet.ON);
        grade.setPostPermission(StatusSet.ON);
        grade.setCategoryPermission(StatusSet.ON);
        grade.setCafe(cafe);
        gradeRepository.save(grade);

        Member member = new Member();
        member.setNickname(nickname);
        member.setCafe(cafe);
        member.setGrade(grade);
        member.setMileage(0);
        member.setUser(user);
        memberRepository.save(member);

        return member;
    }

    public List<Cafe> searchCafe(String keyword) {
        return cafeRepository.searchCafe(keyword);
    }
}
