package com.backend.server.model.repository;

import com.backend.server.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalRestrictionRepository extends JpaRepository<User, Long> {

}
