package com.backend.server.model.entity.equipment;

import com.backend.server.model.entity.BaseTimeEntity;
import com.backend.server.model.entity.User;
import com.backend.server.model.entity.enums.BrokenType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "equipment_broken_history")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class EquipmentBrokenHistory extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "broken_by_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User brokenBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "broken_type", nullable = false)
    private BrokenType brokenType;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id", nullable = false,  foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Equipment equipment;


    @Column(name = "broken_detail", length = 500)
    private String brokenDetail;
}
