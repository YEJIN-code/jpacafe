package jpaark.jpacafe.service;

import jpaark.jpacafe.domain.Comment;
import jpaark.jpacafe.domain.User;
import jpaark.jpacafe.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // 조회 성능 최적화
@RequiredArgsConstructor // final 로 된 걸 생성해줌
public class CommentService {

    private final CommentRepository commentRepository;

    @Transactional(readOnly = false)
    public Long join(Comment comment, Long parentId) {
        // 부모 댓글이 존재하는 경우
        if (parentId != null) {
            Comment parentComment = commentRepository.findOne(parentId);
            if (parentComment != null) {
                parentComment.addChildComment(comment);
            }
        }
        commentRepository.save(comment);

        return comment.getId();
    }

    // 게시물 id와 일치하는 모든 댓글 조회
    public List<Comment> findByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    // 멤버 id와 일치하는 모든 댓글 조회
    public List<Comment> findByMemberId(Long memberId) {
        return commentRepository.findByMemberId(memberId);
    }

}
