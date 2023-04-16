package jpaark.jpacafe.domain;

import jpaark.jpacafe.domain.User;

import javax.persistence.*;

/**
 * bookshelf_id (층수)
 * member_id
 * cafe_name
 * books
 */
public class bookshelf {

    @Id @GeneratedValue
    @Column(name = "bookshelf_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_id")
    private User cafe;

    private int book;

}
