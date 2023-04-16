package jpaark.jpacafe.domain;

import jpaark.jpacafe.domain.Status.MemberGrade;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Cafe {

    @Id @GeneratedValue
    @Column(name = "cafe_id")
    private Long id;

    @OneToMany(mappedBy = "cafe")
    private List<Member> members = new ArrayList<>();

    @OneToMany(mappedBy = "cafe") // 카페에 게시판 리스트가 꽂힘
    private List<Category> c