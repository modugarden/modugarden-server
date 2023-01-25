package com.modugarden.domain.curation.entity;

import com.modugarden.common.entity.BaseTimeEntity;
import com.modugarden.domain.category.repository.entity.InterestCategory;
import com.modugarden.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private InterestCategory category;

    @Builder
    public Curation(Long id, String title, String link, String previewImage, User user, InterestCategory category) {
        this.id = id;
        this.title = title;
        this.link = link;
        this.previewImage = previewImage;
        this.user = user;
        this.category = category;
    }
}
