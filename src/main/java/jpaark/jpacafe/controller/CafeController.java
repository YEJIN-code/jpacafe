package jpaark.jpacafe.controller;

import jpaark.jpacafe.controller.form.CafeForm;
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
    private final UserService userService;
    private final CafeHomeService cafeHomeService;


    @GetMapping("/cafes/newCafe")
    public String newCafe(Model model) {
        model.addAttribute("cafeForm", new CafeForm());
        return "cafes/newCafe";
    }

    @PostMapping("/cafes/newCafe")
    public String createCafe(
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Users loginMember,
            @Valid CafeForm form, BindingResult result, Model model,
            HttpSession session) {

        Cafe cafe = new Cafe();
        cafe.setName(form.getName());
        cafe.setInfo(form.getInfo());
        cafeService.join(cafe);
        model.addAttribute("cafe", cafe);

        log.info("createCafe loginMember id = {}", loginMember.getId());


        Grade normalGrade = new Grade();
        normalGrade.setName("normal");
        normalGrade.setCafe(cafe);
        normalGrade.setCafePermission(StatusSet.OFF);
        normalGrade.setCategoryPermission(StatusSet.OFF);
        normalGrade.setPostPermission(StatusSet.OFF);
        gradeService.join(normalGrade);

        Grade managerGrade = new Grade();
        managerGrade.setName("manager");
        managerGrade.setCafe(cafe);
        managerGrade.setCafePermission(StatusSet.ON);
        managerGrade.setCategoryPermission(StatusSet.ON);
        managerGrade.setPostPermission(StatusSet.ON);
        gradeService.join(managerGrade);

        Member member = new Member();
        member.setUser(loginMember);
        log.info("member.setUser(loginMember) result = {}", member.getUser());
        member.setCafe(cafe);
        member.setGrade(managerGrade);
        member.setNickname(form.getNickName());
        memberService.join(member);

        model.addAttribute("member", member);

        model.addAttribute("user", loginMember);

        model.addAttribute("cafeId", cafe.getId()); // cafeId를 모델에 추가


        return "redirect:/cafeHome?cafeId=" + cafe.getId();

    }

    @GetMapping("/cafeHome")
    public String cafeHome(
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Users loginUser,
            Model model,
            @RequestParam(name = "cafeId") Long cafeId) {
        log.info("cafeHome? cafeId: {}", cafeId); // 로그 추가
        log.info("cafeHome? loginUser: {}", loginUser); // 로그 추가

        cafeHomeService.cafeHomeMethod(loginUser, model, cafeId);

        return "cafes/cafeHome";
    }



    @GetMapping("/search")
    public String searchFunction(@RequestParam(name = "keyword") String keyword,
                                 Model model) {
        List<Cafe> cafeList = cafeService.searchCafe(keyword);
        model.addAttribute("cafe", cafeList);
        return "cafes/search";
    }


}
