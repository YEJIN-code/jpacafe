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
import org.springframework.web.bind.annotation.*;

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
    private final CategoryMarkService categoryMarkService;
    private final CafeHomeService cafeHomeService;

    @GetMapping("/cafes/newCategory")
    public String newCategory(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Users loginUser,
                              @RequestParam(name = "cafeId") Long cafeId,
                              Model model, HttpSession session) {

        cafeHomeService.cafeHomeMethod(loginUser, model, cafeId);

        model.addAttribute("categoryForm", new CategoryForm());
        session.setAttribute("cafeId", cafeId); // cafeId 값을 세션에 설정

        return "cafes/newCategory";
    }


    @PostMapping("/cafes/newCategory")
    public String createCategory(@Valid CategoryForm form, BindingResult result, Model model,
                                 HttpSession session) {
        if (result.hasErrors()) {
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

    @GetMapping("/deleteCategory")
    public String deleteCategory(@RequestParam("categoryId") Long categoryId, @RequestParam(name = "cafeId") Long cafeId,
                                 @RequestParam(name = "categoryTotal") int categoryTotal) {

        if (categoryTotal == 0) {
            categoryService.deleteCategory(categoryId);
        } else {
            return "error";
        }

        return "redirect:/cafeHome?cafeId="+cafeId;
    }

    @GetMapping("/posts")
    public String categoryPost(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Users loginUser,
                               @RequestParam("categoryId") Long categoryId, @RequestParam("cafeId") Long cafeId,
                               Model model) {

        cafeHomeService.cafeHomeMethod(loginUser, model, cafeId);

        List<Post> postList = postService.findByCategoryId(categoryId);
        model.addAttribute("posts", postList);
        Category nowCategory = categoryService.findOne(categoryId);
        model.addAttribute("nowCategory", nowCategory);

        Long markCount = categoryMarkService.countByUserId(loginUser.getId());
        model.addAttribute("markCount", markCount);
        log.info("alreadyMark = {}", markCount);

        List<CategoryMark> categoryMarkList = categoryMarkService.findByUserIdAndCategoryId(loginUser.getId(), categoryId);
        model.addAttribute("alreadyMark", categoryMarkList.size());

        return "cafes/categoryPost";
    }

    @GetMapping("/categoryMark")
    public String categoryMark(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Users loginUser,
                               @RequestParam("categoryId") Long categoryId, @RequestParam("cafeId") Long cafeId) {

        CategoryMark categoryMark = new CategoryMark();

        categoryMark.setCategory(categoryService.findOne(categoryId));
        categoryMark.setUser(loginUser);
        categoryMark.setCafe(cafeService.findOne(cafeId));

        int newPostCount = postService.newPostCountCal(categoryId);
        categoryMark.setNewPostCount(newPostCount);

        categoryMarkService.join(categoryMark);

        return "redirect:/posts?categoryId=" + categoryId + "&cafeId=" + cafeId;

    }

    @GetMapping("/categoryUnMark")
    public String categoryUnMark(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Users loginUser,
                                 @RequestParam("categoryId") Long categoryId, @RequestParam("cafeId") Long cafeId) {

        CategoryMark categoryMark = categoryMarkService.findByUserIdAndCategoryId(loginUser.getId(), categoryId).get(0);
        categoryMarkService.deleteCategoryMark(categoryMark.getId());
        return "redirect:/posts?categoryId=" + categoryId + "&cafeId=" + cafeId;
    }

}
