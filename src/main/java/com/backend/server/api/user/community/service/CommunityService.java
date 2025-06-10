package com.backend.server.api.user.community.service;


import com.backend.server.api.admin.community.dto.CommunityListRequest;
import com.backend.server.api.common.dto.LoginUser;
import com.backend.server.api.user.community.dto.*;
import com.backend.server.model.entity.*;
import com.backend.server.model.entity.equipment.EquipmentCategory;
import com.backend.server.model.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 커뮤니티 게시글과 관련된 비즈니스 로직을 처리하는 서비스 클래스입니다.
 * 게시글 생성, 조회, 수정, 삭제, 추천 등의 기능을 수행합니다.
 */
@Service
@Transactional(readOnly = true) // 읽기 전용 메소드에 대한 트랜잭션 성능 최적화
@RequiredArgsConstructor

public class CommunityService {
    private final CommunityRepository communityRepository; // Community 엔티티 데이터 접근을 위한 Repository
    private final UserRepository userRepository; // User 엔티티 데이터 접근을 위한 Repository (작성자 정보 조회 시 필요)
    private final RecommendationRepository recommendationRepository; // Recommendation 엔티티 데이터 접근을 위한 Repository (추천 기능 시 필요)
    private final BoardCategoryRepository boardCategoryRepository;

    // "개선사항 요구" 게시글의 community_type_id 값 (예시, 실제 값으로 변경 필요)
    private static final Long DEFAULT_COMMUNITY_TYPE_ID = 1L; // TODO: 실제 "개선사항 요구"의 typeId 값으로 변경 필요
    

    public boolean hasUserRecommended(Long postId, Long userId) {
        // RecommendationRepository의 existsByUserIdAndCommunityId 메소드를 호출하여 추천 기록 존재 여부를 확인
        return recommendationRepository.existsByUserIdAndCommunityId(userId, postId);
    }

    /**
     * 새로운 커뮤니티 게시글을 생성합니다.
     *
     * @param authorId 게시글 작성자의 ID
     * @param loginuser 현재 로그인한 사용자 정보 (응답 DTO 생성 시 필요)
     * @return 생성된 게시글 정보가 담긴 CommunityResponse DTO
     */
    @Transactional // 쓰기 작업이므로 트랜잭션 적용
    public CommunityResponse createPost(CreatePostRequest request, Long authorId, LoginUser loginuser) {
        User author = userRepository.findById(authorId)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + authorId));
        BoardCategory category = boardCategoryRepository.findById(request.getCategoryId()).orElseThrow(()->new IllegalArgumentException("카테고리 없음"));
        Community community = Community.builder()
            .title(request.getTitle())
            .content(request.getContent())
            .boardCategory(category)
            .author(author)
            .nickname(author.getNickname())
            .recommend(0)
            .view(0)
            .build();

        Community savedCommunity = communityRepository.save(community);

        return new CommunityResponse(savedCommunity, loginuser);
    }

    @Transactional(readOnly = true)
    public List<CommunityCategoryListResponse> getBoardCategories() {
        List<BoardCategory> categories = boardCategoryRepository.findAll();
        return categories.stream()
                .map(category -> {
                    CommunityCategoryListResponse dto = new CommunityCategoryListResponse();
                    dto.setId(category.getId());
                    dto.setName(category.getName());
                    dto.setDescription(category.getDescription());
                    return dto;
                })
                .collect(Collectors.toList());
    }
    /**
     * 특정 ID를 가진 커뮤니티 게시글의 상세 정보를 조회합니다.
     * 게시글 조회 시 조회수를 1 증가시킵니다.
     *
     * @param postId 조회할 게시글의 ID
     * @param loginuser 현재 로그인한 사용자 정보 (응답 DTO 생성 시 작성자 이름 표시 로직에 사용)
     * @return 조회된 게시글 정보가 담긴 CommunityResponse DTO
     */
    @Transactional // 조회수 증가(쓰기 작업)가 포함되므로 트랜잭션 적용
    public CommunityResponse getPostById(Long postId, LoginUser loginuser) {
        Optional<Community> communityOptional = communityRepository.findById(postId);

        Community community = communityOptional.orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));

        community.setView(community.getView() + 1);
        Community viewedCommunity = communityRepository.save(community);

        return new CommunityResponse(viewedCommunity, loginuser);
    }


    // @Transactional(readOnly = true) // 클래스 레벨에 적용되어 있음
    public CommunityListResponse getPosts(LoginUser loginuser, CommunityListRequest request) {
        Pageable pageable = request.toPageable();

        Specification<Community> spec = CommunitySpecification.filterCommunitiesForUser(request);

        Page<Community> page = communityRepository.findAll(spec, pageable);

        return new CommunityListResponse(page, loginuser);
    }

    @Transactional
    public CommunityResponse updatePost(Long postId,
                                        UpdatePostRequest request,
                                        LoginUser loginUser) {
        Community existing = communityRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));

        // 작성자 검증
        if (!existing.getAuthor().getId().equals(loginUser.getId())) {
            throw new RuntimeException("You are not authorized to update this post.");
        }

        // 제목/내용 업데이트
        existing.setTitle(request.getTitle());
        existing.setContent(request.getContent());

        // 카테고리 업데이트
        BoardCategory category = boardCategoryRepository.findById(request.getCategotyId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + request.getCategotyId()));
        existing.setBoardCategory(category);

        // 변경사항 저장
        Community updated = communityRepository.save(existing);

        return new CommunityResponse(updated, loginUser);
    }

    /**
     * 사용자 권한으로 특정 커뮤니티 게시글을 삭제합니다.
     * 게시글 작성자만 삭제할 수 있도록 권한을 확인합니다.
     *
     * @param postId 삭제할 게시글의 ID
     * @param loginuser 현재 로그인한 사용자 정보 (권한 확인에 사용)
     */
    @Transactional // 쓰기 작업이므로 트랜잭션 적용
    public void deletePost(Long postId, LoginUser loginuser) {
        Community existingCommunity = communityRepository.findById(postId)
             .orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));

        if (!existingCommunity.getAuthor().getId().equals(loginuser.getId())) {
            throw new RuntimeException("You are not authorized to delete this post.");
        }

        communityRepository.deleteById(postId);
    }

    /**
     * 특정 커뮤니티 게시글에 추천합니다.
     * 사용자의 중복 추천을 방지합니다.
     *
     * @param postId 추천할 게시글의 ID
     * @param loginuser 현재 로그인한 사용자 정보 (추천한 사용자 기록에 사용)
     * @return 추천 후 업데이트된 게시글 정보가 담긴 CommunityResponse DTO
     */
    @Transactional // 쓰기 작업(추천 기록 저장, 추천수 증가)이 포함되므로 트랜잭션 적용
    public CommunityResponse recommendPost(Long postId, LoginUser loginuser) {
        Community community = communityRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));

        User user = userRepository.findById(loginuser.getId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + loginuser.getId()));

        if (recommendationRepository.existsByUserIdAndCommunityId(user.getId(), community.getId())) {
            Recommendation recommendation = recommendationRepository.findByUserAndCommunity(user, community);

            recommendationRepository.delete(recommendation);

            community.setRecommend(community.getRecommend() - 1);
            Community updatedCommunity = communityRepository.save(community);
            return null;
        } //1번 더 클릭 시 감소로 변경

        Recommendation recommendation = new Recommendation(null, user, community);

        recommendationRepository.save(recommendation);

        community.setRecommend(community.getRecommend() + 1);
        Community updatedCommunity = communityRepository.save(community);

        return new CommunityResponse(updatedCommunity, loginuser);
    }
}
