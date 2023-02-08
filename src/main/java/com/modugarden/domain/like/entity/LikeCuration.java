package com.modugarden.domain.like.entity;

import com.modugarden.common.entity.BaseTimeEntity;
import com.modugarden.domain.curation.entity.Curation;
import com.modugarden.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class LikeCuration extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curation_id", nullable = false)
    private Curation curation;

    @Builder
    public LikeCuration(User user, Curation curation) {
        this.user = user;
        this.curation = curation;
    }
}
