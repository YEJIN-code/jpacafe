package jpaark.jpacafe.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // FK 이름 설정.
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_id")
    private Cafe cafe;
    // 팀은 회원을 여러명 가질 수 있음 -> member 클래스에서 team은 manyToOne이 됨
    // 유저는 여러 카페를 가질 수 있음 -> 유저 클래스에서 카페는 manyToOne이 됨
    // 즉, 여러개가 꽂히는 곳

    @OneToMany(mappedBy = "member")
    private List<Comment> comments = new ArrayList<>();

    @Column(unique = true)
    private String nickname;

    private int mileage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade_id")
    private Grade grade;


//    private profileImg


    // 연관관계 매핑
    public void setUser(User user) { // 이 클래스의 매니 투 원
        this.user = user;
        user.getMembers().add(this);
    }

    public void setCafe(Cafe cafe) { // 이 클래스의 매니 투 원
        this.cafe = cafe;
        cafe.getMembers().add(this);
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
        grade.getMembers().add(this);
    }

    public static Member createMember(String nickname, Cafe cafe) {
        Member member = new Member();
        member.setNickname(nickname);
        member.setCafe(cafe);
        // Grade를 설정하는 로직을 추가할 수 있습니다.
        // 예를 들어, 기본 Grade를 설정하거나, 랜덤한 Grade를 할당할 수 있습니다.
        member.setGrade(member.grade.defaultGrade());
        return member;
    }



}
