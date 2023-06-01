package jpaark.jpacafe.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Shelf {

    @Id @GeneratedValue
    @Column(name = "shelf_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_id")
    private Cafe cafe;

    private int floor;

    public void setUser(Users user) { // 이 클래스의 매니 투 원
        this.user = user;
        user.getShelves().add(this);
    }

    public void setCafe(Users user) { // 이 클래스의 매니 투 원
        this.cafe = cafe;
        user.getShelves().add(this);
    }
}
