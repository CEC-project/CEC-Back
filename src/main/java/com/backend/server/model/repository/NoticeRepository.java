package com.backend.server.model.repository;

import com.backend.server.model.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface NoticeRepository extends JpaRepository<Notice, Long>, JpaSpecificationExecutor<Notice> {
  boolean existsByTitle(String title);

  boolean existsById(Long id);
}
