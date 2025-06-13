package com.backend.server.model.repository.board;

import com.backend.server.model.entity.Board;
import com.backend.server.model.entity.Recommendation;
import com.backend.server.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {

    // Spring Data JPA의 쿼리 메소드 파싱 기능을 이용하여 메소드 선언만으로 쿼리 자동 생성
    // 특정 user_id와 community_id를 가지는 Recommendation 엔티티가 존재하는지 확인하는 메소드
    // 이 메소드는 CommunityService에서 중복 추천을 확인하는 데 사용
    boolean existsByUserIdAndBoardId(Long userId, Long communityId);

    Recommendation findByUserAndBoard(User user, Board board);
}
