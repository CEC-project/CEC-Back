package com.backend.server.model.repository;

import com.backend.server.model.entity.EquipmentFavorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipmentFavoriteRepository extends JpaRepository<EquipmentFavorite, Long> {
    
    // 사용자의 모든 즐겨찾기 조회
    List<EquipmentFavorite> findByUserId(Long userId);
    
    // 특정 장비에 대한 모든 즐겨찾기 조회
    List<EquipmentFavorite> findByEquipmentId(Long equipmentId);
    
    // 사용자의 특정 장비 즐겨찾기 조회
    Optional<EquipmentFavorite> findByUserIdAndEquipmentId(Long userId, Long equipmentId);
    
    // 사용자의 특정 장비 즐겨찾기 여부 확인
    boolean existsByUserIdAndEquipmentId(Long userId, Long equipmentId);
    
    // 사용자의 특정 장비 즐겨찾기 삭제
    void deleteByUserIdAndEquipmentId(Long userId, Long equipmentId);
} 