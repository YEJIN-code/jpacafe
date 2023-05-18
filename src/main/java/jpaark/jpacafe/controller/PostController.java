package jpaark.jpacafe.controller;

import jpaark.jpacafe.controller.form.CategoryForm;
import jpaark.jpacafe.controller.form.PostForm;
import jpaark.jpacafe.domain.Cafe;
import jpaark.jpacafe.domain.Category;
import jpaark.jpacafe.domain.Member;
import jpaark.jpacafe.domain.Post;
import jpaark.jpacafe.repository.MemberRepository;
import jpaark.jpacafe.service.CafeService;
import jpaark.jpacafe.service.PostService;
import jpaark.jpacafe.session.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final MemberRepository memberRepository;
    private final CafeService cafeService;

//    @GetMapping("/users/main")
//    public List<Post> getMemberCafePosts(Long memberId) {
//        Member member = memberRepository.findOne(memberId);
//        Cafe cafe = member.getCafe();
//        List<Post> latestPosts = cafe.getLatestPosts(3); // 최신 게시물 3개 가져오기 (가상의 메서드)
//
//        return latestPosts;
//    }

    @GetMapping("/cafes/newPost")
    public String newPost(Model model, HttpSession session, @RequestParam(name = "cafeId") Long cafeId) {
        model.addAttribute("postForm", new PostForm());
        session.setAttribute("cafeId", cafeId); // cafeId 값을 세션에 설정

        return "cafes/newPost";
    }

    @PostMapping("/cafes/newPost")
    public String createPost(@Valid PostForm form, BindingResult result, Model model,
                             HttpSession session) {
        if (result.hasErrors()) {
            // 유효성 검사 실패 시 처리할 로직 작성
            return "error";
        }

        Long cafeId = (Long) session.getAttribute("cafeId");

        Post post = new Post();
        post.setCategory();
        post.setCafe(cafeService.findOne(cafeId));
        post.setTitle(form.getTitle());
        post.setContent(form.getContent());
        postService.join(post);

        model.addAttribute("category", category);
        model.addAttribute("cafeId", cafeId);
        model.addAttribute("post", post);

        return "redirect:/postHome";

    }


}
