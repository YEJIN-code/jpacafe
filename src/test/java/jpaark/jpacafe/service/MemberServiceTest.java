package jpaark.jpacafe.service;

import jpaark.jpacafe.domain.Cafe;
import jpaark.jpacafe.domain.Member;
import jpaark.jpacafe.domain.User;
import jpaark.jpacafe.repository.MemberRepository;
import jpaark.jpacafe.repository.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    CafeRepository // 여기해야함...

    @Test(expected = IllegalStateException.class)
    public void 중복닉네임_예외() throws Exception {
        // given
        Member member1 = new Member();
        member1.setNickname("asdf");

        Member member2 = new Member();
        member2.setNickname("asdf");

        memberService.join(member1);
        memberService.join(member2);

        // when & then
        Assert.fail("예외가 발생해야 한다.");
    }


    @Test
    public void 카페_전체멤버_찾기()  throws Exception {
        // Given
        Cafe cafe = new Cafe();
        cafe.setName("정통");



        Member member1 = new Member();
        member1.setNickname("aaa");
        member1.setCafe(cafe);

        Member member2 = new Member();
        member2.setNickname("bbb");
        member2.setCafe(cafe);

        memberService.join(member1);
        memberService.join(member2);

        // When
        List<Member> actualMembers = memberService.findAll(cafe.getId());

        // Then
        assertEquals(2, actualMembers.size());
    }
}