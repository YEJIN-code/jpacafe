package jpaark.jpacafe.domain;

import jpaark.jpacafe.domain.Status.StatusSet;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_sticker")
@Getter @Setter
public class UserSticker {

    @Id @GeneratedValue
    @Column(name = "user_sticker_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @OneToMany(mappedBy = "userSticker")
    private List<Sticker> stickers = new ArrayList<>();


    private StatusSet use;

    public void setUser(Users user) { // 이 클래스의 매니 투 원
        this.user = user;
        user.getUserStickers().add(this);
    }

    public void addSticker(Sticker sticker) {
        stickers.add(sticker);
        sticker.setUserSticker(this);
    }
}
