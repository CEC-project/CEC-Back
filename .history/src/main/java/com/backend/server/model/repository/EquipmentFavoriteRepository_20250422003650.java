package com.backend.server.model.repository;

import com.backend.server.model.entity.EquipmentFavorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipmentFavoriteRepository extends JpaRepository<EquipmentFavorite, Long> {
    
    List<EquipmentFavorite> findByUserId(Long userId);
    
    Optional<EquipmentFavorite> findByUserIdAndEquipmentId(Long userId, Long equipmentId);
    
    boolean existsByUserIdAndEquipmentId(Long userId, Long equipmentId);
    
    void deleteByUserIdAndEquipmentId(Long userId, Long equipmentId);
} 