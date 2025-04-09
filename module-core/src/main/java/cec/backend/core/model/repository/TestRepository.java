package cec.backend.core.model.repository;

import cec.backend.core.model.entity.Test;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<Test, Long> {
    Optional<Test> findById(Long id);
}