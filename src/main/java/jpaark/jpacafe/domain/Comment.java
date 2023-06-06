package jpaark.jpacafe.domain;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.Id;
import jpaark.jpacafe.domain.Comment;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Comment {

    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 작성자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @GeneratedValue
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment; // 부모 댓글

    @OneToMany(mappedBy = "parentComment", orphanRemoval = true)
    @GeneratedValue
    private List<Comment> childComments = new ArrayList<>(); // 자식 댓글들


    private String content;

    private LocalDateTime date;

    public void setPost(Post post) {
        this.post = post;
        if (post != null) {
            post.getComments().add(this);
        }
    }

    // 연관관계 매핑
    public void setMember(Member member) {
        this.member = member;
        if (member != null) {
            jpaark.jpacafe.domain.Comment comment = this;
            member.getComments().add(this);
        }
    }

    public void setParentComment(Comment parentComment) {
        this.parentComment = parentComment;
    }

    public void addChildComment(Comment childComment) {
        childComments.add(childComment);
        childComment.setParentComment(this);
    }
}
