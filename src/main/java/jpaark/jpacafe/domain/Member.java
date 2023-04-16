package jpaark.jpacafe.domain;

import jpaark.jpacafe.domain.Status.MemberGrade;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.xml.stream.events.Comment;
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

    @OneToMany(mappedBy = "member")
    private List<Post> posts = new ArrayList<>();

    private String nickname;

    private int mileage;

    @Enumerated(EnumType.STRING)
    private MemberGrade grade;

//    private profileImg

    
}
