package com.backend.server.model.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "professor", indexes = {@Index(name = "idx_deleted_at_professor", columnList = "deleted_at")})
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Where(clause = "deleted_at IS NULL")
public class Professor extends BaseTimeEntity{
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String description;

    @Builder.Default
    @OneToMany(mappedBy = "professor")
    private List<User> students = new ArrayList<>();
}
