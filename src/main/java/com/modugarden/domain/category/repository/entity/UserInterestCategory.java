package com.modugarden.domain.category.repository.entity;

import com.modugarden.common.entity.BaseTimeEntity;
import com.modugarden.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UserInterestCategory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interest_category_id", nullable = false)
    private InterestCategory category;

    @Builder
    public UserInterestCategory(User user, InterestCategory category) {
        this.user = user;
        this.category = category;
    }
}
