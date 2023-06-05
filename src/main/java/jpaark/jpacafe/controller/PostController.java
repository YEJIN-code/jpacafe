package jpaark.jpacafe.controller;

import jpaark.jpacafe.controller.form.CommentForm;
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
    private final CommentService commentService;
    private final CafeHomeService cafeHomeService;

    @GetMapping("/cafes/newPost")
    public String newPost(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Users loginMember,
                          @RequestParam(name = "cafeId") Long cafeId,
                          Model model) {
        cafeHomeService.cafeHomeMethod(loginMember, model, cafeId);

        model.addAttribute("postForm", new PostForm());
        log.info("hello? cafeId: {}", cafeId); // 로그 추가
        List<Category> categories = categoryService.findAllByCafeId(cafeId);
        model.addAttribute("categories", categories);

        return "cafes/newPost";
    }


    @PostMapping("/cafes/newPost")
    public String createPost(@Valid PostForm form, BindingResult result, Model model,
                             @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Users loginMember,
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

        Users user = userService.findOne(loginMember.getId());
        List<Member> members = memberService.findByCafeIdAndUserId(cafeId, user.getId());

        post.setWriter(members.get(0).getNickname());

        model.addAttribute("cafeId", cafeId);
        model.addAttribute("post", post);
        model.addAttribute("member", members.get(0));
        model.addAttribute("postId", post.getId());

        postService.join(post);


        return "redirect:/cafes/" + post.getId() + "/postHome?cafeId=" + cafeId;
    }

    @GetMapping("/cafes/{postId}/postHome")
    public String postHome(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Users loginMember,
                           @PathVariable Long postId,  @RequestParam Long cafeId,
                           Model model, HttpSession session) {

        Post post = postService.findOne(postId);
        log.info("now view = {}", post.getViewCount());

        Member member = new Member();

        if (loginMember != null) {
            String userId = loginMember.getId();
            List<Member> members = memberService.findByCafeIdAndUserId(cafeId, userId);
            if (members.size()!=0) { // 존재하는 회원
                member = members.get(0);
            } else { // 존재하지 않는 회원

            }
            model.addAttribute("memberNickname", member.getNickname());
        } else {
            // 로그인 정보가 없는 경우에 대한 처리
        }

        postService.updatePostView(postId);

        model.addAttribute("cafeId", cafeId);
        model.addAttribute("post", post);

        List<Comment> comments = commentService.findByPostId(postId);
        model.addAttribute("comments", comments);
        model.addAttribute("commentForm", new CommentForm());

        cafeHomeService.cafeHomeMethod(loginMember, model, cafeId);

        return "cafes/postHome";
    }

    @GetMapping("/modify/{postId}")
    public String modifyPostForm(@PathVariable Long postId, Model model) {

        Post post = postService.findOne(postId);
        PostForm form = new PostForm();
        form.setId(post.getId());
        form.setTitle(post.getTitle());
        Category category = post.getCategory();
        form.setCategory(category.getName());
        form.setContent(post.getContent());

        List<Category> categories = post.getCafe().getCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("form", form);
        model.addAttribute("post", post);
        return "cafes/modifyPost";
    }

    @PostMapping("/modify/{postId}")
    public String modifyPost(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Users loginMember,
                             @ModelAttribute("form") PostForm form,
                             @PathVariable Long postId) {

        Post post = postService.updatePost(postId, form.getTitle(), form.getContent(), form.getCategory());

        return "redirect:/cafes/" + post.getId() + "/postHome?cafeId=" + post.getCafe().getId();
    }

    @DeleteMapping("/delete/{postId}")
    public String deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return "redirect:/cafes/postHome"; // 삭제 후 리다이렉트할 URL 설정
    }


    @GetMapping("/searchPost")
    public String searchPost(
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Users loginUser,
            @RequestParam("keyword") String keyword,
            @RequestParam("option") String option,
            @RequestParam("cafeId") Long cafeId,
            Model model) {
        cafeHomeService.postSearchMethod(loginUser, model, cafeId);


        if ("all".equals(option)) {
            model.addAttribute("posts", postService.findByAllKeyword(keyword));
        } else if ("title".equals(option)) {
            model.addAttribute("posts", postService.findByTitle(keyword));
        } else if ("content".equals(option)) {
            model.addAttribute("posts", postService.findByContent(keyword));
        } else {
            model.addAttribute("posts", null);
        }

        return "cafes/searchPost";
    }


}
