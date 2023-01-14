package com.modugarden.domain.board.entity;

import com.modugarden.common.entity.BaseTimeEntity;
import com.modugarden.domain.category.entity.InterestCategory;
import com.modugarden.domain.user.entity.User;
import lombok.AccessLevel;
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

    @Column(nullable = false, columnDefinition = "TEXT")
    private String boardImage;

    private String location;

    private User user;

    private InterestCategory category;
}
