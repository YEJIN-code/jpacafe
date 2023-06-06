package jpaark.jpacafe.domain;

import jpaark.jpacafe.domain.Status.StatusSet;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Grade {

    @Id @GeneratedValue
    @Column(name = "grade_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_id")
    private Cafe cafe;

    @OneToMany(mappedBy = "grade", orphanRemoval = true)
    private List<Member> members = new ArrayList<>();

    private String name;

    private StatusSet postPermission;

    private StatusSet categoryPermission;

    private StatusSet cafePermission;

    // 연관관계 메소드
    public void setCafe(Cafe cafe) {
        this.cafe = cafe;
        cafe.getGrades().add(this);
    }

    public void addMember(Member member) {
        members.add(member);
        member.setGrade(this);
    }

}
