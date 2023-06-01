package jpaark.jpacafe.controller;

        import jpaark.jpacafe.controller.form.CommentForm;
        import jpaark.jpacafe.domain.Comment;
        import jpaark.jpacafe.domain.Member;
        import jpaark.jpacafe.domain.Post;
        import jpaark.jpacafe.domain.Users;
        import jpaark.jpacafe.service.CommentService;
        import jpaark.jpacafe.service.MemberService;
        import jpaark.jpacafe.service.PostService;
        import jpaark.jpacafe.session.SessionConst;
        import lombok.RequiredArgsConstructor;
        import lombok.extern.slf4j.Slf4j;
        import org.springframework.stereotype.Controller;
        import org.springframework.ui.Model;
        import org.springframework.web.bind.annotation.*;

        import javax.validation.Valid;
        import java.time.LocalDateTime;
        import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;
    private final MemberService memberService;

    @GetMapping("/comment")
    public String showComment(@PathVariable Long commentId, Model model) {
        model.addAttribute("commentForm", new CommentForm());
        return "comment";
    }

    @PostMapping("/comment")
    public String createComment(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Users loginUser,
                                @RequestParam Long postId,
                                @RequestParam(required = false) Long parentCommentId,
                                @Valid CommentForm form, Model model) {

        Comment comment = new Comment();

        Post post = postService.findOne(postId);
        Long cafeId = post.getCafe().getId();

        List<Member> memberList = memberService.findByCafeIdAndUserId(cafeId, loginUser.getId());
        Member member = memberList.get(0);

        log.info("comment content: {}", form.getContent());

        comment.setContent(form.getContent());
        comment.setPost(postService.findOne(postId));
        comment.setMember(member);
        comment.setDate(LocalDateTime.now());
        commentService.join(comment, parentCommentId);

        return "redirect:/cafes/" + postId + "/postHome?cafeId=" + cafeId;
    }

}
