package jpaark.jpacafe.domain;

import jpaark.jpacafe.domain.Status.StatusSet;
import jpaark.jpacafe.domain.Sticker;
import jpaark.jpacafe.domain.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "sticker_book")
@Getter @Setter
public class StickerBook {

    @Id @GeneratedValue
    @Column(name = "sticker_book_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sticker_id")
    private Sticker sticker;

    private StatusSet use;
}
