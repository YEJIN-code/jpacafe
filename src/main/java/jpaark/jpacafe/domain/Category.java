package jpaark.jpacafe.domain;

import jpaark.jpacafe.domain.Status.StatusSet;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Category {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_id")
    private Cafe cafe;

    @OneToMany(mappedBy = "category")
    private List<Post> posts = new ArrayList<>();

    private String name;

    private int total;

    @Enumerated(EnumType.STRING)
    private StatusSet status;

    // 연관관계 설정
    public void setCafe(Cafe cafe) { // 이 클래스의 매니 투 원
        this.cafe = cafe;
        cafe.getCategories().add(this);
    }

    public void addPost(Post post) { // 이 클래스의 원 투 매니
        posts.add(post);
        post.setCategory(this);
    }


}
