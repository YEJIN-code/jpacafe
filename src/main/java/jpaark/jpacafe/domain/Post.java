package jpaark.jpacafe.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * post_id
 * category_id
 * user_id
 * title
 * content
 * view
 * date
 */

@Entity
@Getter @Setter
public class Post {

    @Id @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 작성자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_id")
    private Cafe cafe;


    private String title; // 제목

    private String content; // 본문

    private int viewCount; // 조회수

    private LocalDateTime dateTime; // 작성일

    private String writer; // 작성 멤버 닉네임

    @PrePersist
    public void setDateTime() {
        this.dateTime = LocalDateTime.now(); // 현재 시간으로 설정
    }


    // 연관관계 매핑
    public void setMember(User user) { // 이 클래스의 매니 투 원
        this.user = user;
        user.getPosts().add(this);
    }

    public void setCategory(Category category) { // 이 클래스의 매니 투 원
        this.category = category;
        category.getPosts().add(this);
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setPost(this);
    }

    // 연관관계 설정 메서드
    public void setCafe(Cafe cafe) {
        this.cafe = cafe;
        if (cafe != null) {
            cafe.getPosts().add(this);
        }
    }
}
