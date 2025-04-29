package com.backend.server.model.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.server.model.entity.ClassRoomFavorite;

@Repository
public interface ClassRoomFavoriteRepository extends JpaRepository<ClassRoomFavorite, Long> {
    List<ClassRoomFavorite> findByUserId(Long userId);
    void deleteByUserIdAndClassRoomId(Long userId, Long classRoomId);
}
