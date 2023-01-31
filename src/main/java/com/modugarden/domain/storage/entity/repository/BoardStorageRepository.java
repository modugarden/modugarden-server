package com.modugarden.domain.storage.entity.repository;

import com.modugarden.domain.board.entity.Board;
import com.modugarden.domain.board.entity.BoardImage;
import com.modugarden.domain.like.entity.LikeBoard;
import com.modugarden.domain.storage.entity.BoardStorage;
import com.modugarden.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardStorageRepository extends JpaRepository<BoardStorage, Long> {
    Optional<BoardStorage> findByUserAndBoard(User user, Board board);

    Optional<BoardStorage> deleteAllByBoard_Id(Long id);
}
