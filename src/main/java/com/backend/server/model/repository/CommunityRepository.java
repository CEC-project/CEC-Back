package com.backend.server.model.repository;

import com.backend.server.model.entity.Community;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Long>{
     // 필요하다면 여기에 추가적인 쿼리 메소드를 선언할 수 있습니다.
    // 예시: 특정 community_type_id를 가진 게시글 목록을 페이지네이션하여 조회
    Page<Community> findAllByType(String type, Pageable pageable);
    Page<Community> findAllByTypeId(Long community_type_id, Pageable pageable);

    // 예시: 작성자(User)로 게시글 목록 조회
    // List<Community> findByAuthor(User author); // User 엔티티 필요
}
