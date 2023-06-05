package jpaark.jpacafe.controller;

import jpaark.jpacafe.domain.Grade;
import jpaark.jpacafe.domain.Member;
import jpaark.jpacafe.service.GradeService;
import jpaark.jpacafe.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
public class GradeController {

    private final GradeService gradeService;
    private final MemberService memberService;



    @GetMapping("/changeGrade")
    public String changeGrade(@RequestParam("selectGrade") String selectGrade, @RequestParam("memberId") Long memberId, @RequestParam("cafeId") Long cafeId) {

        Member member = memberService.findOne(memberId);
        Grade grade = gradeService.findByCafeIdAndName(cafeId, selectGrade);

        gradeService.changeGrade(selectGrade, memberId, cafeId);

        return "redirect:/cafes/memberList?cafeId=" + cafeId;

    }
}
