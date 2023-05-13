package jpaark.jpacafe.controller;

import jpaark.jpacafe.domain.User;
import jpaark.jpacafe.service.UserService;
import jpaark.jpacafe.session.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

//    @GetMapping("/login")
//    public String loginForm(@ModelAttribute("loginForm") LoginForm form) {
//        return "/login/loginForm";
//    }

    @GetMapping("/users/new")
    public String createForm(Model model) {
        model.addAttribute("userForm", new UserForm());
        return "users/createUserForm";
    }


    @PostMapping("/users/new")
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
//
//    @GetMapping("/users/login")
//    public String loginForm(@ModelAttribute("userForm") UserForm form) {
//        return "users/loginForm";
//    }
//
//    @PostMapping("/users/login")
//    public String login(@Valid @ModelAttribute UserForm form, BindingResult bindingResult,
//                          @RequestParam(defaultValue = "/") String redirectURL,
//                          HttpServletRequest request) {
//        if (bindingResult.hasErrors()) {
//            return "users/loginForm";
//        }
//
//        // 성공로직
//
//        System.out.println("form = " + form);
//        User loginUser = userService.login(form.getId(), form.getPassword());
//        log.info("login? {}", loginUser);
//
//        if (loginUser == null) {
//            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
//            return "users/loginForm";
//        }
//
//        // 로그인 성공 처리
//        // 세션이 있으면 있는 걸 반환, 없으면 신규 세션 생성
//        HttpSession session = request.getSession();
//        // 세션에 로그인 회원 정보 보관
//        session.setAttribute(SessionConst.LOGIN_MEMBER, loginUser);
//
//        return  "redirect:/";
//    }
//
//    private static String expireCookie(HttpServletResponse response, String cookieName) {
//        Cookie cookie = new Cookie(cookieName, null);
//        cookie.setMaxAge(0);
//        response.addCookie(cookie);
//        return "redirect:/";
//    }
}
