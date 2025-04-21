package com.backend.server.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "equipment_favorites")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipmentFavorite extends BaseTimeEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "equipment_id", nullable = false)
    private Long equipmentId;
    
    @Builder.Default
    @Column(name = "favorite_time", nullable = false)
    private LocalDateTime favoriteTime = LocalDateTime.now();
}
