package jpaark.jpacafe.domain;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;import java.util.List;
@Entity
@Getter @Setter
public class Users {
    @Id
    @Column(name = "user_id")
    private String id;

    private String password;

    private String name;

    @OneToMany(mappedBy = "user",  fetch = FetchType.EAGER) // 일대다 관계이고 관계의 주인은 아님
    private List<Member> members = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<CategoryMark> categoryMarks = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Shelf> shelves = new ArrayList<>();

    private String email;

    private LocalDate birthDate;
    // private profileImg; // 아직 안함

    @OneToMany(mappedBy = "user")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<UserSticker> userStickers = new ArrayList<>();

    // 연관관계 매핑
    public void addMember(Member member) {
        members.add(member);
        member.setUser(this);
    }

    public void addShelf(Shelf shelve) {
        shelves.add(shelve);
        shelve.setUser(this);
    }

    public void addPost(Post post) {
        posts.add(post);
        post.setUser(this);
    }

    public void addStickerBook(UserSticker userSticker) {
        userStickers.add(userSticker);
        userSticker.setUser(this);
    }

    public String getPassword() {
        return password;
    }

    public void addCategoryMark(CategoryMark categoryMark) {
        categoryMarks.add(categoryMark);
        categoryMark.setUser(this);
    }


}


