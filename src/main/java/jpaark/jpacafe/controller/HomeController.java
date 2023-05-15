package jpaark.jpacafe.controller;

import jpaark.jpacafe.domain.Member;
import jpaark.jpacafe.domain.User;
import jpaark.jpacafe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class HomeController {
//
//    private final UserRepository userRepository;
//
//    @RequestMapping("/")
//    public String home() {
//        log.info("home controller");
//        return "home";
//    }
//
//    @GetMapping("/")
//    public String homeLogin(
//            @CookieValue(name = "memberId", required = false) String id, Model model) {
//        if (id == null) {
//            return "home";
//        }
////로그인
//        User loginMember = userRepository.findOne(id);
//        if (loginMember == null) {
//            return "home";
//        }
//        model.addAttribute("member", loginMember);
//        return "loginHome";
//    }
}