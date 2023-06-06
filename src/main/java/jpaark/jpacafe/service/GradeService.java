package jpaark.jpacafe.service;

import jpaark.jpacafe.domain.Grade;
import jpaark.jpacafe.domain.Member;
import jpaark.jpacafe.domain.Post;
import jpaark.jpacafe.repository.GradeRepository;
import jpaark.jpacafe.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor // final 로 된 걸 생성해줌
public class GradeService {

    private final GradeRepository gradeRepository;
    private final MemberRepository memberRepository;

    // 등급 생성
    @Transactional(readOnly = false) // 쓰기에는 readOnly true 이면 안되므로 다시 정의
    public Long join(Grade grade) {
//        validateDuplicateGrade(grade); // 중복 유저 검증
        gradeRepository.save(grade);
        return grade.getId();
    }

//    private void validateDuplicateGrade(Grade grade) {
//        List<Grade> findGrades = gradeRepository.findByName(grade.getName());
//        if (!findGrades.isEmpty()) {
//            throw new IllegalStateException("이미 존재하는 등급입니다.");
//        }
//    }

    @Transactional
    public void changeGrade(String gradeName, Long memberId, Long cafeId) {

        Grade grade = gradeRepository.findByCafeIdAndName(cafeId, gradeName);
        Member member = memberRepository.findOne(memberId);

        member.setGrade(grade);
    }

    public List<Grade> findByCafeId(Long cafeId) {
        return gradeRepository.findByCafeId(cafeId);
    }

    public List<Grade> findByName(String name) {
        return gradeRepository.findByName(name);
    }

    public List<Grade> findByMemberId(Long id) {
        return gradeRepository.findByMemberId(id);
    }

    public List<Grade> findNormalGradesByCafeId(Long cafeId) {
        return gradeRepository.findNormalGradesByCafeId(cafeId);
    }

    public Grade findByCafeIdAndName(Long cafeId, String name) {
        return gradeRepository.findByCafeIdAndName(cafeId, name);
    }
}
