package com.modugarden.domain.curation.repository;

import com.modugarden.domain.curation.entity.Curation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurationRepository extends JpaRepository<Curation, Long> {
    Page<Curation> findAllByUser_Id(Long user_id, Pageable pageable);
}
