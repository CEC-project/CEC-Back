package com.backend.server.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "equipment_model")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class EquipmentModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; 


    private boolean available;
    //카테고리 아이디 명시한거임 왜냐면 카테고리 아이디 참조해서 조회해야 하니까
    private Long categoryId;

    private String imageUrl;
    private Integer rentalRestrictedGrades;
}