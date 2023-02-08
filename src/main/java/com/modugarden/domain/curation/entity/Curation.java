package com.modugarden.domain.curation.entity;

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

    @Column
    private Long likeNum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private InterestCategory category;

    @Builder
    public Curation(String title, String link, String previewImage,Long likeNum, User user, InterestCategory category) {
        this.title = title;
        this.link = link;
        this.previewImage = previewImage;
        this.likeNum = likeNum;
        this.user = user;
        this.category = category;
    }

    public void addLike(){
        this.likeNum += 1;
    }

    public void delLike(){
        this.likeNum -= 1;
    }
}
