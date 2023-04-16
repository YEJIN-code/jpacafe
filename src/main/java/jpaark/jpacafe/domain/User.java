package jpaark.jpacafe.domain;
import jdk.dynalink.linker.LinkerServices;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;import java.util.List;
@Entity
@Getter
@Setter
public class User {
    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    private String password;

    private String name;

    @OneToMany(mappedBy = "user") // 일대다 관계이고 관계의 주인은 아님
    private List<Member> members = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Shelf> shelves = new ArrayList<>();

    private String nickname;

    private String email;

    private LocalDateTime birthDate;
    //    private profileImg; // 아직 안배움

    @OneToMany(mappedBy = "user")
    private List<Post> posts = new ArrayList<>();

}


