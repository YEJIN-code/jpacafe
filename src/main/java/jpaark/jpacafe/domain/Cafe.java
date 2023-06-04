package jpaark.jpacafe.domain;

import jpaark.jpacafe.domain.Status.StatusSet;
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
    private List<Category> categories = new ArrayList<>();

    @OneToMany(mappedBy = "cafe")
    private List<Sticker> stickers = new ArrayList<>();

    @OneToMany(mappedBy = "cafe")
    private List<Grade> grades = new ArrayList<>();

    @OneToMany(mappedBy = "cafe")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "cafe")
    private List<CategoryMark> categoryMarks = new ArrayList<>();


    @Column(unique = true)
    private String name;

    private String Info;

    private int mileageRate;

//    icon;


    // 연관관계 설정
    public void addMember(Member member) {
        members.add(member);
        member.setCafe(this);
    }

    public void addCategory(Category category) {
        categories.add(category);
        category.setCafe(this);
    }

    public void addSticker(Sticker sticker) {
        stickers.add(sticker);
        sticker.setCafe(this);
    }

    public void addGrade(Grade grade) {
        grades.add(grade);
        grade.setCafe(this);
    }
    public void addPost(Post post) {
        posts.add(post);
        post.setCafe(this);
    }

    public void removePost(Post post) {
        posts.remove(post);
        post.setCafe(null);
    }

    public void addCategoryMark(CategoryMark categoryMark) {
        categoryMarks.add(categoryMark);
        categoryMark.setCafe(this);
    }


    // == 생성 메서드 == //
    public static Cafe createCafe(String name, String info, int mileageRate) {
        Cafe cafe = new Cafe();
        cafe.setName(name);
        cafe.setInfo(info);
        cafe.setMileageRate(mileageRate);
        return cafe;
    }


    public static Member createManager(String nickname, Cafe cafe, Users user, Grade grade) {
        Member member = new Member();
        member.setNickname(nickname);
        member.setCafe(cafe);
        member.setUser(user);
        member.setGrade(grade);

        return member;
    }


    /**
     * 카페 삭제
     */
    public void isCanDelete(Member member) {
        // 카페 삭제 권한이 없으면 삭제 불가
        if (member.getGrade().getCafePermission() != StatusSet.ON) {
            throw new IllegalStateException("카페 매니저가 아니면 삭제할 수 없습니다.");
        }
    }


    public List<Post> getLatestPosts(int count) {
        int size = Math.min(count, posts.size());
        return posts.subList(0, size);
    }


}
