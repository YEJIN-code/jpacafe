package jpaark.jpacafe.controller;

import jpaark.jpacafe.controller.form.CategoryForm;
import jpaark.jpacafe.domain.*;
import jpaark.jpacafe.domain.Status.StatusSet;
import jpaark.jpacafe.service.*;
import jpaark.jpacafe.session.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CafeService cafeService;
    private final PostService postService;
    private final MemberService memberService;
    private final GradeService gradeService;

    @GetMapping("/cafes/newCategory")
    public String newCategory(Model model, HttpSession session, @RequestParam(name = "cafeId") Long cafeId) {
        model.addAttribute("categoryForm", new CategoryForm());
        session.setAttribute("cafeId", cafeId); // cafeId 값을 세션에 설정

        return "cafes/newCategory";
    }


    @PostMapping("/cafes/newCategory")
    public String createCategory(@Valid CategoryForm form, BindingResult result, Model model,
                                 HttpSession session) {
        if (result.hasErrors()) {
            // 유효성 검사 실패 시 처리할 로직 작성
            return "error";
        }

        Long cafeId = (Long) session.getAttribute("cafeId"); // 세션에서 cafeId 값을 가져옴
        Cafe cafe = cafeService.findOne(cafeId);


        Category category = new Category();
        category.setCafe(cafe);
        category.setName(form.getName());
        categoryService.join(category);
        model.addAttribute("category", category);

        return "redirect:/cafeHome?cafeId="+cafeId;
    }

    @GetMapping("/posts")
    public String categoryPost(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Users loginUser,
                               @RequestParam("categoryId") Long categoryId, @RequestParam("cafeId") Long cafeId,
                               Model model) {
        Cafe cafe = cafeService.findOne(cafeId); // cafeId로 Cafe 객체 조회

        List<Post> postList = postService.findByCategoryId(categoryId);
        model.addAttribute("cafe", cafe);
        model.addAttribute("posts", postList);
        List<Category> categories = categoryService.findAllByCafeId(cafeId);
        model.addAttribute("categories", categories);
        model.addAttribute("user", loginUser);
        String nowCategory = categoryService.findOne(categoryId).getName();
        model.addAttribute("nowCategory", nowCategory);

        List<Member> memberList = memberService.findByCafeIdAndUserId(cafeId, loginUser.getId());
        if (!memberList.isEmpty()) {
            model.addAttribute("member", memberList.get(0)); // 멤버 정보 보내줌
            List<Grade> grades = gradeService.findByMemberId(memberList.get(0).getId()); // 등급 정보 불러옴
            model.addAttribute("grade", grades.get(0)); // 등급도 보내줌
        } else {
            model.addAttribute("member", null); // null 을 보내줌
            Grade guest = new Grade();
            guest.setCafePermission(StatusSet.OFF);
            guest.setCategoryPermission(StatusSet.OFF);
            guest.setPostPermission(StatusSet.OFF);
            model.addAttribute("grade", guest); // 등급은 게스트로 보내줌
        }

        return "cafes/categoryPost"; // 실제로 보여줄 뷰 이름을 반환
    }


}
