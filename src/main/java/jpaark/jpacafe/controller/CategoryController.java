package jpaark.jpacafe.controller;

import jpaark.jpacafe.controller.form.CategoryForm;
import jpaark.jpacafe.domain.Cafe;
import jpaark.jpacafe.domain.Category;
import jpaark.jpacafe.service.CafeService;
import jpaark.jpacafe.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CafeService cafeService;

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

}
