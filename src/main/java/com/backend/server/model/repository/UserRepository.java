package com.backend.server.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.server.model.entity.User;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUserStudentNumber(String id);
}
