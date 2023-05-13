package jpaark.jpacafe.service;

import jpaark.jpacafe.domain.Comment;
import jpaark.jpacafe.domain.Member;
import jpaark.jpacafe.domain.Post;
import jpaark.jpacafe.repository.CommentRepository;
import jpaark.jpacafe.repository.MemberRepository;
import jpaark.jpacafe.repository.PostRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class CommentServiceTest {
    @Autowired
    CommentService commentService;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PostRepository postRepository;

    @Test
    public void 부모댓글저장() {
        // Given
        Comment comment = new Comment();
        comment.setContent("부모 댓글입니다.");

        // When
        Long savedCommentId = commentService.join(comment, null);

        // Then
        Comment savedComment = commentRepository.findOne(savedCommentId);
        assertEquals(comment, savedComment);
    }

    @Test
    public void 자식댓글저장() {
        // Given
        Comment parentComment = new Comment();
        parentComment.setContent("부모 댓글입니다.");

        Comment childComment = new Comment();
        childComment.setContent("자식 댓글입니다.");

        // When
        Long savedParentCommentId = commentService.join(parentComment, null);
        Long savedChildCommentId = commentService.join(childComment, savedParentCommentId);

        // Then
        Comment savedParentComment = commentRepository.findOne(savedParentCommentId);
        Comment savedChildComment = commentRepository.findOne(savedChildCommentId);

        assertEquals(parentComment, savedParentComment);
        assertEquals(childComment, savedChildComment);
        assertEquals(parentComment, childComment.getParentComment());
    }

    @Test
    public void 게시물별_댓글_검색() {
        // Given
        Post post1 = new Post();
        Post post2 = new Post();

        postRepository.save(post1);
        postRepository.save(post2);

        Comment comment1 = new Comment();
        comment1.setContent("댓글 1");
        comment1.setPost(post1);

        Comment comment2 = new Comment();
        comment2.setContent("댓글 2");
        comment2.setPost(post1);

        Comment comment3 = new Comment();
        comment3.setContent("댓글 3");
        comment3.setPost(post2);

        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment3);

        // When
        List<Comment> commentsWithPostId1 = commentService.findByPostId(post1.getId());
        List<Comment> commentsWithPostId2 = commentService.findByPostId(post2.getId());
        List<Comment> commentsWithPostId3 = commentService.findByPostId(3L);

        // Then
        assertEquals(2, commentsWithPostId1.size());
        assertEquals(1, commentsWithPostId2.size());
        assertEquals(0, commentsWithPostId3.size());
    }

    @Test
    public void 멤버별_댓글저장() {
        // Given
        Member member1 = new Member();
        Member member2 = new Member();

        memberRepository.save(member1);
        memberRepository.save(member2);

        Comment comment1 = new Comment();
        comment1.setContent("댓글 1");
        comment1.setMember(member1);

        Comment comment2 = new Comment();
        comment2.setContent("댓글 2");
        comment2.setMember(member1);

        Comment comment3 = new Comment();
        comment3.setContent("댓글 3");
        comment3.setMember(member2);

        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment3);

        // When
        List<Comment> commentsWithMemberId1 = commentService.findByMemberId(member1.getId());
        List<Comment> commentsWithMemberId2 = commentService.findByMemberId(member2.getId());
        List<Comment> commentsWithMemberId3 = commentService.findByMemberId(3L);

        // Then
        assertEquals(2, commentsWithMemberId1.size());
        assertEquals(1, commentsWithMemberId2.size());
        assertEquals(0, commentsWithMemberId3.size());
    }

    @Test
    public void 멤버가_작성한_댓글_확인() {
        // Given
        Member member = new Member();
        memberRepository.save(member);

        Comment parentComment1 = new Comment();
        parentComment1.setContent("부모댓글1");
        parentComment1.setMember(member);
        commentRepository.save(parentComment1);

        Comment parentComment2 = new Comment();
        parentComment2.setContent("부모댓글2");
        commentRepository.save(parentComment2);

        Comment childComment1 = new Comment();
        childComment1.setContent("자식댓글1");
        childComment1.setMember(member);
        childComment1.setParentComment(parentComment2);
        commentRepository.save(childComment1);

        // When
        List<Comment> commentsByMember = commentRepository.findByMemberId(member.getId());

        // Then
        assertEquals(2, commentsByMember.size());
        assertTrue(commentsByMember.contains(parentComment1));
        assertTrue(commentsByMember.contains(childComment1));
    }


    }