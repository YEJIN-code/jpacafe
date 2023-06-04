package jpaark.jpacafe.controller;

import jpaark.jpacafe.controller.form.LoginForm;
import jpaark.jpacafe.controller.form.UserForm;
import jpaark.jpacafe.domain.CategoryMark;
import jpaark.jpacafe.domain.Member;
import jpaark.jpacafe.domain.Users;
import jpaark.jpacafe.repository.CafeRepository;
import jpaark.jpacafe.repository.MemberRepository;
import jpaark.jpacafe.repository.UserRepository;
import jpaark.jpacafe.service.*;
import jpaark.jpacafe.session.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final CategoryMarkService categoryMarkService;
    private final PostService postService;
    private final CategoryService categoryService;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form, Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "users/loginForm";
    }

    @GetMapping("/loginHome")
    public String loginHome() {
        return "/users/loginHome";
    }



    @GetMapping("/users/new")
    public String createForm(Model model) {
        model.addAttribute("userForm", new UserForm());
        return "users/createUserForm";
    }


    @PostMapping("users/new")
    public String create(@Valid UserForm form, BindingResult result) {

        if (result.hasErrors()) {
            return "users/createUserForm";
        }

        Users user = new Users();
        user.setId(form.getId());
        user.setPassword(form.getPassword());
        user.setName(form.getName());
        user.setEmail(form.getEmail());
        user.setBirthDate(form.getBirthDate());

        userService.join(user);
        return "redirect:/";
    }

    @GetMapping("/users/login")
    public String loginForm(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "users/loginForm";
    }

    @PostMapping("/users/login")
    public String login(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult,
                        @RequestParam(defaultValue = "/") String redirectURL,
                        HttpServletRequest request, Model model) {
        if (bindingResult.hasErrors()) {
            return "users/loginForm";
        }

        // 성공로직

        System.out.println("form = " + form);
        Users loginMember = userService.login(form.getLoginId(), form.getPassword());
        log.info("login? {}", loginMember);

        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "users/loginForm";
        }

        // 로그인 성공 처리
        // 세션이 있으면 있는 걸 반환, 없으면 신규 세션 생성
        HttpSession session = request.getSession();
        // 세션에 로그인 회원 정보 보관
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        model.addAttribute("user", loginMember);

        List<Member> allMember = userRepository.findAllMember(loginMember.getId());

        model.addAttribute("members", allMember);

        log.info("Session created: {}", session.getId());
        List<CategoryMark> markList = categoryMarkService.findByUserId(loginMember.getId());
        log.info("cafeHome? markListSize: {}", markList.size()); // 로그 추가


        int[] buttonSize = new int[]{0, 0, 0, 0, 0};
        List<String> cafeNameList = new ArrayList<>(5);
        List<String> categoryNameList = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) {
            cafeNameList.add("0");
            categoryNameList.add("0");
            buttonSize[i] = 0;
            log.info("cafeNameList.get() = {}", cafeNameList.get(i));
        }

        int size = markList.size();
        log.info("markList.size = {}", size);
        for (int i = 0; i < size; i++) { // 즐찾 리스트 사이즈만큼 반복

            newPostCountUpdate(markList.get(i));

            cafeNameList.set(i, markList.get(i).getCafe().getName());
            categoryNameList.set(i, markList.get(i).getCategory().getName());
            buttonSize[i] = markList.get(i).getNewPostCount(); // 새 글 수 저장
            if (buttonSize[i]>5) { // 새 글 수가 5를 넘으면
                buttonSize[i] = 5; // 5로 맞춰줌
            }
            log.info("cafeNameList.get() = {}", cafeNameList.get(i));
            log.info("categoryNameList.get() = {}", categoryNameList.get(i));

        }

        log.info("buttonSize = {} {} {} {} {}",buttonSize[0], buttonSize[1], buttonSize[2], buttonSize[3], buttonSize[4]);

        model.addAttribute("markList", markList);
        model.addAttribute("buttonSize", buttonSize);
        model.addAttribute("cafeNameList", cafeNameList);
        model.addAttribute("categoryNameList", categoryNameList);

        model.addAttribute("markList", markList);

        return "/users/index"; // 원하는 경로로 변경
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        //세션을 삭제한다.
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }


    @GetMapping("/")
    public String homeLoginV3Spring(
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false)
            Users loginMember,
            Model model) {
//세션에 회원 데이터가 없으면 home
        if (loginMember == null) {
            return "/users/loginHome";
        }
//세션이 유지되면 로그인으로 이동
        model.addAttribute("user", loginMember);

        List<Member> allMember = userRepository.findAllMember(loginMember.getId());
        model.addAttribute("members", allMember);
        List<CategoryMark> markList = categoryMarkService.findByUserId(loginMember.getId()); // 즐찾 카테고리 리스트

        int[] buttonSize = new int[]{0, 0, 0, 0, 0};
        List<String> cafeNameList = new ArrayList<>(5);
        List<String> categoryNameList = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) {
            cafeNameList.add("0");
            categoryNameList.add("0");
            buttonSize[i] = 0;
            log.info("cafeNameList.get() = {}", cafeNameList.get(i));
        }

        int size = markList.size();
        log.info("markList.size = {}", size);
        for (int i = 0; i < size; i++) { // 즐찾 리스트 사이즈만큼 반복

            newPostCountUpdate(markList.get(i));

            cafeNameList.set(i, markList.get(i).getCafe().getName());
            categoryNameList.set(i, markList.get(i).getCategory().getName());
            buttonSize[i] = markList.get(i).getNewPostCount(); // 새 글 수 저장
            if (buttonSize[i]>5) { // 새 글 수가 5를 넘으면
                buttonSize[i] = 5; // 5로 맞춰줌
            }
            log.info("cafeNameList.get() = {}", cafeNameList.get(i));
            log.info("categoryNameList.get() = {}", categoryNameList.get(i));
        }

        log.info("buttonSize = {} {} {} {} {}",buttonSize[0], buttonSize[1], buttonSize[2], buttonSize[3], buttonSize[4]);

        model.addAttribute("markList", markList);
        model.addAttribute("buttonSize", buttonSize);
        model.addAttribute("cafeNameList", cafeNameList);
        model.addAttribute("categoryNameList", categoryNameList);

        return "/users/index";
    }

    private void newPostCountUpdate(CategoryMark markList) {

        int newPostCountCal = postService.newPostCountCal(markList.getCategory().getId());
        categoryMarkService.updateCategoryMark(markList.getId(), newPostCountCal);
        log.info("categoryId {}'s updatePostCount = {}", markList.getCategory().getName(), markList.getNewPostCount());
    }

    @GetMapping("/profile")
    public String goProfile(@RequestParam(name = "loginId") String loginId,
                            Model model) {
        Users user = userService.getUser(loginId);
        model.addAttribute("user", user);
        return "/users/profile";
    }
}
