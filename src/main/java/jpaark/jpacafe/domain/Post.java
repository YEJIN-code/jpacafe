package jpaark.jpacafe.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.xml.stream.events.Comment;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
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
    @JoinColumn(name = "member_id")
    private Member member; // 작성자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    private String title; // 제목

    private String content; // 본문

    private int viewCount; // 조회수

    private LocalDateTime dateTime; // 작성일
}
