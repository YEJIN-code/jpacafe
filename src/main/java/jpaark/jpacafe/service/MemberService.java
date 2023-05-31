package jpaark.jpacafe.service;

import jpaark.jpacafe.domain.Member;
import jpaark.jpacafe.domain.User;
import jpaark.jpacafe.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true) // 조회 성능 최적화
@RequiredArgsConstructor // final 로 된 걸 생성해줌
public class MemberService {

    private final MemberRepository memberRepository;


    // 카페 멤버 가입
    @Transactional(readOnly = false) // 쓰기에는 readOnly true 이면 안되므로 다시 정의
    public Long join(Member member) {
//        validateDuplicateMember(member); // 중복 유저 검증
        memberRepository.save(member);
        return member.getId();
    }

//    private void validateDuplicateMember(Member member) {
//        List<Member> findMembers = memberRepository.findListByNickname(member.getNickname());
//        if (!findMembers.isEmpty()) {
//            throw new IllegalStateException("이미 존재하는 닉네임입니다.");
//        }
//    }

//    // 카페 멤버 가입
//    @Transactional(readOnly = false) // 쓰기에는 readOnly true 이면 안되므로 다시 정의
//    public Long join(Member member, Cafe cafe) {
//        validateDuplicateMember(member, cafe); // 중복 유저 검증
//        memberRepository.save(member);
//        return member.getId();
//    }
//
//    private void validateDuplicateMember(Member member, Cafe cafe) {
//        List<Member> findMembers = memberRepository.findByCafeIdAndNickname(cafe.getId(), member.getNickname());
//        if (!findMembers.isEmpty()) {
//            throw new IllegalStateException("이미 존재하는 닉네임입니다.");
//        }
//    }


    // 멤버 전체 조회
    public List<Member> findAll(Long cafeId) {
        return memberRepository.findAll(cafeId);
    }

    // 개별 멤버 조회
    public Member findOne(String nickname) {
        return memberRepository.findByNickname(nickname);
    }

    public Member getMember(Long memberId) {
        Member member = memberRepository.findOne(memberId);
        if (member == null) {
            throw new IllegalArgumentException("Member not found");
        }

        return member;
    }

    public List<Member> findByCafeIdAndUserId(Long cafeId, String userId) {
        log.info("findByCafeIdAndUserId - cafeId: {}, userId: {}", cafeId, userId); // 로그 추가
        log.info("findByCafeIdAndUserId - members size: {}", (memberRepository.findByCafeIdAndUserId(cafeId, userId)).size()); // 로그 추가

        return memberRepository.findByCafeIdAndUserId(cafeId, userId);
    }

    public int deleteMember(Long memberId) {
        return memberRepository.deleteMember(memberId);
    }
}
