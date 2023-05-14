package jpaark.jpacafe.controller;

import jpaark.jpacafe.domain.Member;
import jpaark.jpacafe.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/member/{memberId}")
    public String getMemberDetails(@PathVariable Long memberId, Model model) {
        Member member = memberService.getMember(memberId);

        if (member == null) {
            // 회원이 존재하지 않는 경우에 대한 처리
            // 예를 들어, 오류 페이지로 이동하거나 적절한 메시지를 설정할 수 있습니다.
            return "error-page";
        }

        // 회원이 존재하는 경우 모델에 추가
        model.addAttribute("member", member);

        return "member-details";
    }
}
