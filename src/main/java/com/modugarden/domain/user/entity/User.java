package com.modugarden.domain.user.entity;

import com.modugarden.common.entity.BaseTimeEntity;
import com.modugarden.domain.board.entity.Board;
import com.modugarden.domain.curation.entity.Curation;
import com.modugarden.domain.user.entity.enums.UserAuthority;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(length = 15, nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String birth;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserAuthority authority;

    @Column(columnDefinition = "TEXT")
    private String profileImg;

    @OneToMany(mappedBy = "user")
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Curation> curations = new ArrayList<>();
}
