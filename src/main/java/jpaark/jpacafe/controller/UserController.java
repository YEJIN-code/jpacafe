package jpaark.jpacafe.controller;

import jpaark.jpacafe.controller.form.LoginForm;
import jpaark.jpacafe.controller.form.UserForm;
import jpaark.jpacafe.domain.Member;
import jpaark.jpacafe.domain.User;
import jpaark.jpacafe.repository.CafeRepository;
import jpaark.jpacafe.repository.MemberRepository;
import jpaark.jpacafe.repository.UserRepository;
import jpaark.jpacafe.service.MemberService;
import jpaark.jpacafe.service.UserService;
import jpaark.jpacafe.session.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final CafeRepository cafeRepository;
    private final MemberService memberService;
    private final MemberRepository memberRepository;

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

        User user = new User();
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
        User loginUser = userService.login(form.getLoginId(), form.getPassword());
        log.info("login? {}", loginUser);

        if (loginUser == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "users/loginForm";
        }

        // 로그인 성공 처리
        // 세션이 있으면 있는 걸 반환, 없으면 신규 세션 생성
        HttpSession session = request.getSession();
        // 세션에 로그인 회원 정보 보관
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginUser);

        model.addAttribute("user", loginUser);

        List<Member> allMember = userRepository.findAllMember(loginUser.getId());

        model.addAttribute("members", allMember);

        log.info("Session created: {}", session.getId());

        return "/users/index"; // 원하는 경로로 변경
    }


    private static String expireCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/loginHome";
    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        expireCookie(response, "memberId");
        return "redirect:/loginHome";
    }


    @GetMapping("/")
    public String homeLoginV3Spring(
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false)
            User loginMember,
            Model model) {
//세션에 회원 데이터가 없으면 home
        if (loginMember == null) {
            return "/users/loginHome";
        }
//세션이 유지되면 로그인으로 이동
        model.addAttribute("user", loginMember);

        List<Member> allMember = userRepository.findAllMember(loginMember.getId());
        model.addAttribute("members", allMember);
        return "/users/index";
    }

}
