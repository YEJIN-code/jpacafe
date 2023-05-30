package jpaark.jpacafe.controller;

import jpaark.jpacafe.controller.form.PostForm;
import jpaark.jpacafe.domain.*;
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
public class PostController {

    private final PostService postService;
    private final MemberRepository memberRepository;
    private final CafeService cafeService;
    private final CategoryService categoryService;
    private final MemberService memberService;
    private final UserService userService;

    @GetMapping("/cafes/newPost")
    public String newPost(Model model, HttpSession session, @RequestParam(name = "cafeId") Long cafeId) {
        model.addAttribute("postForm", new PostForm());
        session.setAttribute("cafeId", cafeId); // cafeId 값을 세션에 설정
        log.info("hello? cafeId: {}", cafeId); // 로그 추가
        List<Category> categories = categoryService.findAllByCafeId(cafeId);
        model.addAttribute("categories", categories);

        return "cafes/newPost";
    }


    @PostMapping("/cafes/newPost")
    public String createPost(@Valid PostForm form, BindingResult result, Model model,
                             @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) User loginMember,
                             @RequestParam(name = "category") String categoryName,
                             HttpSession session) {
        if (result.hasErrors()) {
            // 유효성 검사 실패 시 처리할 로직 작성
            return "error";
        }

        Long cafeId = (Long) session.getAttribute("cafeId");

        Post post = new Post();
        post.setCafe(cafeService.findOne(cafeId));
        post.setTitle(form.getTitle());
        post.setContent(form.getContent());
        List<Category> categories = categoryService.findByName(categoryName);
        post.setCategory(categories.get(0));
        post.setComments(null);
        post.setDateTime();
        post.setUser(loginMember);
        postService.join(post);

        User user = userService.findOne(loginMember.getId());
        log.info("hello? cafeId: {}, userId: {}", cafeId, user.getId()); // 로그 추가
        List<Member> members = memberService.findByCafeIdAndUserId(cafeId, user.getId());

        model.addAttribute("cafeId", cafeId);
        model.addAttribute("post", post);
        model.addAttribute("member", members.get(0));
        model.addAttribute("postId", post.getId());

        session.setAttribute("postId", post.getId()); // postId 값을 세션에 설정

        return "redirect:/cafes/" + post.getId() + "/postHome?cafeId=" + cafeId;


    }

    @GetMapping("/cafes/{postId}/postHome")
    public String postHome(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) User loginMember,
                           @PathVariable Long postId,  @RequestParam Long cafeId,
                           Model model, HttpSession session) {

        log.info("hello? cafeId: {}", cafeId);
        Post post = postService.findOne(postId);

        if (loginMember != null) {
            String userId = loginMember.getId();
            log.info("hello? userId: {}", userId);
            List<Member> members = memberService.findByCafeIdAndUserId(cafeId, userId);
            Member member = members.get(0);
            log.info("hello? findByCafeIdAndUserId - members size: {}", members.size());

            model.addAttribute("memberNickname", member.getNickname());
        } else {
            // 로그인 정보가 없는 경우에 대한 처리
        }

        model.addAttribute("cafeId", cafeId);
        model.addAttribute("post", post);

        return "cafes/postHome";
    }

    // 다른 메서드와 필드 생략...

}
