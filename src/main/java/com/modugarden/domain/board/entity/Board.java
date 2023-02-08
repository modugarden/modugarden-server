package com.modugarden.domain.board.entity;

import com.modugarden.common.entity.BaseTimeEntity;
import com.modugarden.domain.category.entity.InterestCategory;
import com.modugarden.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 40, nullable = false)
    private String title;

    @Column
    private Long like_num;

    @Column
    private String preview_img;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private InterestCategory category;

    @Builder
    public Board(String title,Long like_num,String preview_img, User user, InterestCategory category) {
        this.title = title;
        this.preview_img = preview_img;
        this.like_num = like_num;
        this.user = user;
        this.category = category;
    }

    public void addLike(){
        this.like_num += 1;
    }

    public void delLike(){
        this.like_num -= 1;
    }
}
