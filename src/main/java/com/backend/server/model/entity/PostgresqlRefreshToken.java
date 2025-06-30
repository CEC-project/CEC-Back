package com.backend.server.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "refresh_token")
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class PostgresqlRefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String key;

    @Column(nullable = false)
    private String value;

    private Long expiresAt;

}
