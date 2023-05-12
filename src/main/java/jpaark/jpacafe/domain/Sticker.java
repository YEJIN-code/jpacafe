package jpaark.jpacafe.domain;

import jpaark.jpacafe.domain.Cafe;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Sticker {

    @Id @GeneratedValue
    @Column(name = "sticker_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_id")
    private Cafe cafe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_sticker_id")
    private UserSticker userSticker;

    private String name;

    // img

    private int cost;

    public void setCafe(Cafe cafe) { // 이 클래스의 매니 투 원
        this.cafe = cafe;
        cafe.getStickers().add(this);
    }

    public void setUserSticker(UserSticker usersticker) { // 이 클래스의 매니 투 원
        this.userSticker = usersticker;
        usersticker.getStickers().add(this);
    }
}
