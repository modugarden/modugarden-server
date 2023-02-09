package com.modugarden.domain.storage.repository;

import com.modugarden.domain.curation.entity.Curation;
import com.modugarden.domain.storage.entity.CurationStorage;
import com.modugarden.domain.user.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface CurationStorageRepository extends JpaRepository<CurationStorage, Long> {
    Optional<CurationStorage> findByUserAndCuration(User user, Curation curation);

    Optional<CurationStorage> deleteAllByCuration(Curation curation);

    Long deleteByUser(User user);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM CurationStorage cs \n" +
            "WHERE :user_id = cs.user.id and cs IN \n" +
            "(SELECT cs FROM CurationStorage cs \n" +
            "LEFT JOIN cs.curation c \n" +
            "WHERE :block_user_id = c.user.id)")
    void deleteAllByUser_Id(@Param("user_id") Long user_id, @Param("block_user_id") Long block_user_id);
}
