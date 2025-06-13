package com.backend.server.model.repository;

import com.backend.server.model.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long>{

    @EntityGraph(attributePaths = {"author", "boardCategory"})
    Page<Board> findAll(Specification<Board> spec, Pageable pageable);

}
