package com.modugarden.domain.curation.entity;

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
public class Curation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 40, nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String link;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String previewImage;

    private User user;

    private InterestCategory category;
}
