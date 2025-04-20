package com.backend.server.model.repository;

import com.backend.server.model.entity.Equipment;
import com.backend.server.model.entity.enums.RentalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long>, JpaSpecificationExecutor<Equipment> {
    List<Equipment> findByCategory(String category);
    List<Equipment> findByRenterId(Integer renterId);
    List<Equipment> findByManagerName(String managerName);
    List<Equipment> findByRentalStatus(RentalStatus rentalStatus);
    
    @Query("SELECT e FROM Equipment e LEFT JOIN FETCH e.rentalHistories WHERE e.id = :id")
    Optional<Equipment> findByIdWithRentalHistories(@Param("id") Long id);
    
    @Query("SELECT DISTINCT e FROM Equipment e WHERE e.id IN :ids")
    List<Equipment> findAllByIdIn(@Param("ids") List<Long> ids);
    
    @Query("SELECT e FROM Equipment e WHERE e.id IN (SELECT f.equipmentId FROM Favorite f WHERE f.userId = :userId)")
    List<Equipment> findAllFavoritesByUserId(@Param("userId") Long userId);
    
    @Query("SELECT COUNT(e) > 0 FROM Equipment e WHERE e.id = :equipmentId AND e.available = true")
    boolean isEquipmentAvailable(@Param("equipmentId") Long equipmentId);
} 