package com.modugarden.domain.storage.repository;

import com.modugarden.domain.board.entity.Board;
import com.modugarden.domain.curation.entity.Curation;
import com.modugarden.domain.storage.entity.BoardStorage;
import com.modugarden.domain.user.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface BoardStorageRepository extends JpaRepository<BoardStorage, Long> {
    Optional<BoardStorage> findByUserAndBoard(User user, Board board);

    Optional<BoardStorage> deleteAllByBoard(Board board);

    Long deleteByUser(User user);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM BoardStorage bs \n" +
            "WHERE bs.user.id = :user_id and bs.board.id in " +
            "(SELECT b.id FROM Board b where b.user.id = :block_user_id)")
    void deleteAllByUser_Id(@Param("user_id") Long user_id, @Param("block_user_id") Long block_user_id);
}
