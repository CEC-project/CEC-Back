package com.backend.server.model.repository;

import com.backend.server.model.entity.RentalRestriction;
import com.backend.server.model.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalRestrictionRepository extends JpaRepository<RentalRestriction, Long> {

    @Query("""
    SELECT DISTINCT r
    FROM RentalRestriction r
    WHERE r.user IN :users""")
    List<RentalRestriction> findAllInUser(List<User> users);
}
