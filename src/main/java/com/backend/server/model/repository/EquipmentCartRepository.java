package com.backend.server.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.server.model.entity.EquipmentCart;

public interface EquipmentCartRepository extends JpaRepository<EquipmentCart, Long>{
    boolean existsByUserIdAndEquipmentId(Long userId, Long equipmentId);
}
