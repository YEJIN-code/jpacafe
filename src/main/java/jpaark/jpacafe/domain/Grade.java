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

    @OneToMany(mappedBy = "grade")
    private List<Member> members = new ArrayList<>();

    @Column(unique = true)
    private String name;

    private StatusSet postPermission;