package jpaark.jpacafe.domain;

import jpaark.jpacafe.domain.Status.StatusSet;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Slf4j
public class Category {

    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    @OneToMany(mappedBy = "category", orphanRemoval = true)
    private List<CategoryMark> categoryMarks = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_id")
    private Cafe cafe;

    @OneToMany(mappedBy = "category", orphanRemoval = true)
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

    public void addCategoryMark(CategoryMark categoryMark) {
        categoryMarks.add(categoryMark);
        categoryMark.setCategory(this);
    }

    public void setTotalPlus() {
        this.setTotal(++this.total);
    }

    public void setTotalMinus() {
        this.setTotal(--this.total);
    }

}
