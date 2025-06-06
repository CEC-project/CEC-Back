package com.backend.server.model.repository;

import com.backend.server.model.entity.Community;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Long>{

    @EntityGraph(attributePaths = {"author", "boardCategory"})
    Page<Community> findAll(Specification<Community> spec, Pageable pageable);

    Page<Community> findAllByTypeId(Long community_type_id, Pageable pageable);
}
