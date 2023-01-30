package com.modugarden.domain.like.repository;

import com.modugarden.domain.board.entity.Board;
import com.modugarden.domain.curation.entity.Curation;
import com.modugarden.domain.like.entity.LikeBoard;
import com.modugarden.domain.like.entity.LikeCuration;
import com.modugarden.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeBoardRepository extends JpaRepository<LikeBoard, Long> {
    Optional<LikeCuration> findByUserAndBoard(User user, Board board);
}
