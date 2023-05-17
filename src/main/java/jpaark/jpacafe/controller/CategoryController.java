package jpaark.jpacafe.controller;

import jpaark.jpacafe.controller.form.CafeForm;
import jpaark.jpacafe.controller.form.CategoryForm;
import jpaark.jpacafe.domain.Cafe;
import jpaark.jpacafe.domain.Category;
import jpaark.jpacafe.domain.Member;
import jpaark.jpacafe.domain.User;
import jpaark.jpacafe.service.CafeService;
import jpaark.jpacafe.service.CategoryService;
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

@Slf4j
@Controller
@RequiredArgsConstructor
public class CategoryController {

    private CategoryService categoryService;
    private CafeService cafeService;

    @GetMapping("/cafes/newCategory")
    public String newCategory(Model model) {
        model.addAttribute("categoryForm", new CategoryForm());
        return "cafes/newCategory";
    }

    @PostMapping("/cafes/newCategory")
    public String createCategory(@Valid CategoryForm form, BindingResult result, Model model,
                                 @RequestParam(name = "cafeId") Long cafeId,
            HttpSession session) {
        Cafe cafe = cafeService.findOne(cafeId);

        Category category = new Category();
        category.setCafe(cafe);
        category.setName(form.getName());
        categoryService.join(category);
        model.addAttribute("category", category);

        return "redirect:/";
    }
}
