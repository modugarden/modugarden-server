package com.modugarden.domain.storage.entity.repository;

import com.modugarden.domain.curation.entity.Curation;
import com.modugarden.domain.storage.entity.CurationStorage;
import com.modugarden.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurationStorageRepository extends JpaRepository<CurationStorage, Long> {
    Optional<CurationStorage> findByUserAndCuration(User user, Curation curation);

    Optional<CurationStorage> deleteAllByCuration(Curation curation);

    Long deleteByUser(User user);
}
