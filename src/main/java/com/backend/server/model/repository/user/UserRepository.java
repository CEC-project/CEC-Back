package com.backend.server.model.repository.user;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.backend.server.model.entity.User;
import com.backend.server.model.entity.enums.Role;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    // 유저 페이지네이션시 N+1 방지
    @EntityGraph(attributePaths = {"professor"})
    Page<User> findAll(Specification<User> spec, Pageable pageable);

    Optional<User> findByEmail(String email);
    Optional<User> findByStudentNumber(String studentNumber);
    List<User> findAllByRole(Role role);

    List<User> findByRoleIn(Role... roles);

    @EntityGraph(attributePaths = {"professor"})
    List<User> findByRoleInOrderByNameAsc(Collection<Role> roles);

    @Query(value = "SELECT * FROM users WHERE user_id = :id", nativeQuery = true)
    Optional<User> findByIdIncludingDeleted(@Param("id") Long id);

    @Modifying
    @Query("delete from User u where u.deletedAt <= :cutoff")
    void deleteByDeletedAtBefore(@Param("cutoff") LocalDateTime cutoff);
}
