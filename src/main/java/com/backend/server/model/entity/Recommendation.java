package com.backend.server.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "recommendations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recommendation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // User와 Many-to-One 관계 설정
    // 한 명의 사용자는 여러 추천을 할 수 있습니다. (Recommendation : User = N : 1)
    @ManyToOne(fetch = FetchType.LAZY) // 지연 로딩 설정 (Recommendation 조회 시 User 엔티티를 바로 가져오지 않음)
    @JoinColumn(name = "user_id", nullable = false) // 외래 키 컬럼 이름을 user_id로 지정하고 NOT NULL 설정
    private User user;

    // Community와 Many-to-One 관계 설정
    // 하나의 게시글은 여러 추천을 받을 수 있습니다. (Recommendation : Community = N : 1)
    @ManyToOne(fetch = FetchType.LAZY) // 지연 로딩 설정
    @JoinColumn(name = "community_id", nullable = false) // 외래 키 컬럼 이름을 community_id로 지정하고 NOT NULL 설정
    private Community community;
}
