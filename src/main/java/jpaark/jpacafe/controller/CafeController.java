package jpaark.jpacafe.controller;

import jpaark.jpacafe.domain.*;
import jpaark.jpacafe.domain.Status.StatusSet;
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
        session.setAttribute("cafe", cafe);

        model.addAttribute("member", member);

        return "redirect:/cafeHome";
    }

    @GetMapping("/cafeHome")
    public String cafeHome(
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) User loginMember,
            Model model,
            HttpSession session) {
        Cafe cafe = (Cafe) session.getAttribute("cafe");

        List<Post> postList = postService.findByCafeId(cafe.getId());
        model.addAttribute("cafe", cafe);
        model.addAttribute("posts", postList);
        List<Cafe> categories = cafeService.findAll(cafe.getId());
        model.addAttribute("categories", categories);

        model.addAttribute("member", loginMember);

        return "/cafes/cafeHome";
    }
}
