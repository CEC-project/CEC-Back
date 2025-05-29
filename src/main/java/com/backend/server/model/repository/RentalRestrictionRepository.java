package com.backend.server.model.repository;

import com.backend.server.model.entity.RentalRestriction;
import com.backend.server.model.entity.User;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalRestrictionRepository extends
        JpaRepository<RentalRestriction, Long>,
        JpaSpecificationExecutor<RentalRestriction> {

    @EntityGraph(attributePaths = {"user"})
    Page<RentalRestriction> findAll(Specification<RentalRestriction> spec, Pageable pageable);

    @Query("""
    SELECT DISTINCT r
    FROM RentalRestriction r
    WHERE r.user IN :users""")
    List<RentalRestriction> findAllInUser(List<User> users);
}
