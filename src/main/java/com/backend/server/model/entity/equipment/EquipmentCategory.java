package com.backend.server.model.entity.equipment;

import com.backend.server.model.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "equipment_category")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
//장비 분류 엔티티
public class EquipmentCategory extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "max_rental_count", nullable = false)
    private Integer maxRentalCount;
    @Column(name = "english_code", nullable = false)
    private String englishCode;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EquipmentModel> models;
}