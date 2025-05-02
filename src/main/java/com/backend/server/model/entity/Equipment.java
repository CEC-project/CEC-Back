package com.backend.server.model.entity;

import jakarta.persistence.*;
import lombok.*;
import com.backend.server.model.entity.enums.Status;

@Entity
@Table(name = "equipment")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Equipment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Column(name = "model_id", nullable = false)
    private Long modelId;

    @Column(nullable = false)
    private Long quantity;

    @Column(name = "serial_number", unique = true)
    private Long serialNumber;

    @Column(name = "manager_id")
    private Long managerId;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "restriction_grade")
    private Long restrictionGrade;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(nullable = false)
    private boolean available;

    @Enumerated(EnumType.STRING)
    @Column(name = "rental_status", nullable = false)
    private Status rentalStatus;

    @Column(name = "rental_count", nullable = false)
    private Long rentalCount;

    @Column(name = "broken_count", nullable = false)
    private Long brokenCount;

    @Column(name = "repair_count", nullable = false)
    private Long repairCount;
}
