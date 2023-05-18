package jpaark.jpacafe.controller;

import jpaark.jpacafe.controller.form.CafeForm;
import jpaark.jpacafe.domain.*;
import jpaark.jpacafe.repository.CafeRepository;
import jpaark.jpacafe.repository.MemberRepository;
import jpaark.jpacafe.service.*;
import jpaark.jpacafe.session.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CafeController {

    private final CafeRepository cafeRepository;
    private final CafeService cafeService;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final GradeService gradeService;
    private final PostService postService;
    private final CategoryService categoryService;
    private final UserService userService;


    @GetMapping("/cafes/newCafe")
    public String newCafe(Model model) {
        model.addAttribute("cafeForm", new CafeForm());
        return "cafes/newCafe";
    }

    @PostMapping("/cafes/newCafe")
    public String createCafe(
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) User loginMember,
            @Valid CafeForm form, BindingResult result, Model model,
            HttpSession session) {

        Cafe cafe = new Cafe();
        cafe.setName(form.getName());
        cafe.setInfo(form.getInfo());
        cafeService.join(cafe);
        model.addAttribute("cafe", cafe);

        Member member = cafeService.createCafe(loginMember.getId(), cafe.getId(), form.getNickName());
        memberService.join(member);
        session.setAttribute("cafe", cafe);
        model.addAttribute("member", member);

        model.addAttribute("user", loginMember);

        model.addAttribute("cafeId", cafe.getId()); // cafeId를 모델에 추가


        return "redirect:/";
    }

    @GetMapping("/cafeHome")
    public String cafeHome(
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) User loginMember,
            Model model,
            @RequestParam(name = "cafeId") Long cafeId) {

        Cafe cafe = cafeService.findOne(cafeId); // cafeId로 Cafe 객체 조회
        List<Post> postList = postService.findByCafeId(cafeId);
        model.addAttribute("cafe", cafe);
        model.addAttribute("posts", postList);
        List<Category> categories = categoryService.findAllByCafeId(cafeId);
        model.addAttribute("categories", categories);
        model.addAttribute("user", loginMember);

        List<Member> member = memberService.findByCafeIdAndUserId(cafeId, loginMember.getId());

        List<Grade> grades = gradeService.findByMemberId(member.get(0).getId());

        model.addAttribute("grade", grades.get(0));

        return "/cafes/cafeHome";
    }

}
