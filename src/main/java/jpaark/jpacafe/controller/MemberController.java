package jpaark.jpacafe.controller;

import jpaark.jpacafe.controller.form.MemberForm;
import jpaark.jpacafe.domain.Cafe;
import jpaark.jpacafe.domain.Grade;
import jpaark.jpacafe.domain.Member;
import jpaark.jpacafe.domain.Users;
import jpaark.jpacafe.service.CafeHomeService;
import jpaark.jpacafe.service.CafeService;
import jpaark.jpacafe.service.GradeService;
import jpaark.jpacafe.service.MemberService;
import jpaark.jpacafe.session.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final GradeService gradeService;
    private final MemberService memberService;
    private final CafeService cafeService;
    private final CafeHomeService cafeHomeService;

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

    @GetMapping("/cafes/join")
    public String joinCafe(Model model, @RequestParam("cafeId") Long cafeId,
                           @SessionAttribute(name = "loginMember", required = false) Users loginMember) {
        log.info("memberController? cafeId: {}", cafeId); // 로그 추가
        model.addAttribute("memberForm", new MemberForm());
        model.addAttribute("cafeId", cafeId); // 수정된 부분
        model.addAttribute("user", loginMember);
        return "cafes/join";
    }

    @PostMapping("/cafes/join")
    public String joinCafe(
            @Valid MemberForm form, BindingResult result, Model model,
            @RequestParam("cafeId") Long cafeId,
            @SessionAttribute(name = "loginMember", required = false) Users loginMember
            ) {

        log.info("memberController? cafeId: {}", cafeId); // 로그 추가
        Cafe cafe = cafeService.findOne(cafeId);

        List<Grade> gradeList = gradeService.findNormalGradesByCafeId(cafeId);
        log.info("gradeList 0: {}", gradeList.get(0).getName());
        Grade grade = gradeList.get(0);

        Users user = loginMember;
        log.info("memberController? loginMember id: {}", user.getId()); // 로그 추가

        Member member = new Member();
        member.setUser(user);
        member.setCafe(cafe);
        member.setGrade(grade);
        member.setNickname(form.getNickName());
        memberService.join(member);


        model.addAttribute("member", member);
        model.addAttribute("user", loginMember);
        model.addAttribute("cafeId", cafe.getId()); // cafeId를 모델에 추가

        return "redirect:/cafeHome?cafeId=" + cafe.getId();
    }


    @PostMapping("/cafes/deleteMember")
    public String deleteMember(@RequestParam("memberId") Long memberId,
                          @SessionAttribute(name = "loginMember", required = false) Users loginMember) {
        log.info("memberController? Member id: {}", memberId); // 로그 추가
        memberService.deleteMember(memberId);


        return "redirect:/";
    }

    @GetMapping("/cafes/memberList")
    public String memberList(@RequestParam("cafeId") Long cafeId,
                             @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Users loginUser,
                             Model model) {
        List<Member> memberList = memberService.findAll(cafeId);
        model.addAttribute("memberList", memberList);

        cafeHomeService.cafeHomeMethod(loginUser, model, cafeId);

        return "cafes/memberList";
    }
}
