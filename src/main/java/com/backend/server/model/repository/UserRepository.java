package com.backend.server.model.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.server.model.entity.User;
import com.backend.server.model.entity.enums.Role;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    // 유저 페이지네이션시 N+1 방지
    @EntityGraph(attributePaths = {"professor"})
    Page<User> findAll(Specification<User> spec, Pageable pageable);

    Optional<User> findByEmail(String email);
    Optional<User> findByStudentNumber(String studentNumber);
    List<User> findByRole(Role role);
    List<User> findByRoleIn(Role... roles);

    @EntityGraph(attributePaths = {"professor"})
    List<User> findByRoleInOrderByNameAsc(Collection<Role> roles);
}
