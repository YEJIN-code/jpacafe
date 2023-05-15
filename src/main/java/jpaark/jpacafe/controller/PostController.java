package jpaark.jpacafe.controller;

import jpaark.jpacafe.domain.Cafe;
import jpaark.jpacafe.domain.Member;
import jpaark.jpacafe.domain.Post;
import jpaark.jpacafe.repository.MemberRepository;
import jpaark.jpacafe.service.PostService;
import jpaark.jpacafe.session.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final MemberRepository memberRepository;

//    @GetMapping("/users/main")
//    public List<Post> getMemberCafePosts(Long memberId) {
//        Member member = memberRepository.findOne(memberId);
//        Cafe cafe = member.getCafe();
//        List<Post> latestPosts = cafe.getLatestPosts(3); // 최신 게시물 3개 가져오기 (가상의 메서드)
//
//        return latestPosts;
//    }

}
