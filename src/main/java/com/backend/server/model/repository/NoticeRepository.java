package com.backend.server.model.repository;

import com.backend.server.model.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
  boolean existsByTitle(String title);
  boolean existsById(Long id);
}
