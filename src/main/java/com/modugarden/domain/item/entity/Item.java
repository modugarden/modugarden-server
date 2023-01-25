package com.modugarden.domain.item.entity;

import com.modugarden.common.entity.BaseTimeEntity;
import com.modugarden.domain.category.repository.entity.InterestCategory;
import com.modugarden.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Item extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 25, nullable = false)
    private String name;

    @Column(length = 200, nullable = false)
    private String content;

    @Column(nullable = false)
    private Long price;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String itemImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private InterestCategory category;
}
