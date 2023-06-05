package jpaark.jpacafe.service;

import jpaark.jpacafe.domain.*;
import jpaark.jpacafe.domain.Status.StatusSet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.List;

@Service
@Transactional(readOnly = true) // 조회 성능 최적화
@RequiredArgsConstructor // final 로 된 걸 생성해줌
public class CafeHomeService {

    private final CafeService cafeService;
    private final PostService postService;
    private final CategoryService categoryService;
    private final GradeService gradeService;
    private final MemberService memberService;

    public void cafeHomeMethod(Users loginUser, Model model, Long cafeId) {
        Cafe cafe = cafeService.findOne(cafeId); // cafeId로 Cafe 객체 조회
        List<Post> postList = postService.findByCafeId(cafeId);
        model.addAttribute("cafe", cafe);
        model.addAttribute("totalMember", memberService.findAll(cafeId).size());
        model.addAttribute("posts", postList);
        List<Category> categories = categoryService.findAllByCafeId(cafeId);
        model.addAttribute("categories", categories);

        model.addAttribute("user", loginUser);

        List<Member> memberList = memberService.findByCafeIdAndUserId(cafeId, loginUser.getId());
        if (!memberList.isEmpty()) {
            model.addAttribute("member", memberList.get(0)); // 멤버 정보 보내줌
            List<Grade> grades = gradeService.findByMemberId(memberList.get(0).getId()); // 등급 정보 불러옴
            model.addAttribute("grade", grades.get(0)); // 등급도 보내줌
        } else {
            model.addAttribute("member", null); // null 을 보내줌
            Grade guest = new Grade();
            guest.setCafePermission(StatusSet.OFF);
            guest.setCategoryPermission(StatusSet.OFF);
            guest.setPostPermission(StatusSet.OFF);
            model.addAttribute("grade", guest); // 등급은 게스트로 보내줌
        }
    }

    public void postSearchMethod(Users loginUser, Model model, Long cafeId) {
        Cafe cafe = cafeService.findOne(cafeId); // cafeId로 Cafe 객체 조회
        model.addAttribute("cafe", cafe);
        List<Category> categories = categoryService.findAllByCafeId(cafeId);
        model.addAttribute("categories", categories);

        model.addAttribute("user", loginUser);

        List<Member> memberList = memberService.findByCafeIdAndUserId(cafeId, loginUser.getId());
        if (!memberList.isEmpty()) {
            model.addAttribute("member", memberList.get(0)); // 멤버 정보 보내줌
            List<Grade> grades = gradeService.findByMemberId(memberList.get(0).getId()); // 등급 정보 불러옴
            model.addAttribute("grade", grades.get(0)); // 등급도 보내줌
        } else {
            model.addAttribute("member", null); // null 을 보내줌
            Grade guest = new Grade();
            guest.setCafePermission(StatusSet.OFF);
            guest.setCategoryPermission(StatusSet.OFF);
            guest.setPostPermission(StatusSet.OFF);
            model.addAttribute("grade", guest); // 등급은 게스트로 보내줌
        }
    }
}
