package com.backend.server.api.user.community.service;

import com.backend.server.api.common.dto.LoginUser;
import com.backend.server.api.user.community.dto.CommunityListResponse;
import com.backend.server.api.user.community.dto.CommunityResponse;
import com.backend.server.api.user.community.dto.CreatePostRequest;
import com.backend.server.model.entity.Community;
import com.backend.server.model.entity.Recommendation;
import com.backend.server.model.entity.User;
import com.backend.server.model.repository.CommunityRepository;
import com.backend.server.model.repository.RecommendationRepository;
import com.backend.server.model.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 커뮤니티 게시글과 관련된 비즈니스 로직을 처리하는 서비스 클래스입니다.
 * 게시글 생성, 조회, 수정, 삭제, 추천 등의 기능을 수행합니다.
 */
@Service
@Transactional(readOnly = true) // 읽기 전용 메소드에 대한 트랜잭션 성능 최적화
public class CommunityService {

    private final CommunityRepository communityRepository; // Community 엔티티 데이터 접근을 위한 Repository
    private final UserRepository userRepository; // User 엔티티 데이터 접근을 위한 Repository (작성자 정보 조회 시 필요)
    private final RecommendationRepository recommendationRepository; // Recommendation 엔티티 데이터 접근을 위한 Repository (추천 기능 시 필요)

    // "개선사항 요구" 게시글의 community_type_id 값 (예시, 실제 값으로 변경 필요)
    private static final Long DEFAULT_COMMUNITY_TYPE_ID = 1L; // TODO: 실제 "개선사항 요구"의 typeId 값으로 변경 필요
    
    /**
     * CommunityService의 생성자입니다.
     * 필요한 Repository 인스턴스들을 주입받습니다.
     *
     * @param communityRepository CommunityRepository 인스턴스
     * @param userRepository UserRepository 인스턴스
     * @param recommendationRepository RecommendationRepository 인스턴스
     */
    public CommunityService(CommunityRepository communityRepository, UserRepository userRepository, RecommendationRepository recommendationRepository) {
        this.communityRepository = communityRepository;
        this.userRepository = userRepository;
        this.recommendationRepository = recommendationRepository;
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

        Community community = Community.builder()
            .title(request.getTitle())
            .content(request.getContent())
            .type(request.getType())
            .typeId(request.getCommunityTypeId())
            .author(author)
            .nickname(author.getNickname())
            .recommend(0)
            .view(0)
            .build();

        Community savedCommunity = communityRepository.save(community);

        return new CommunityResponse(savedCommunity, loginuser);
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

    /**
     * 커뮤니티 게시글 목록을 페이지네이션하여 조회합니다.
     * 특정 커뮤니티 타입 ID로 필터링하여 조회할 수 있습니다.
     * typeId가 제공되지 않으면 기본값인 "개선사항 요구" 게시글 목록을 조회합니다.
     *
     * @param pageable 페이지 정보 (페이지 번호, 페이지 크기, 정렬 조건 등)
     * @param loginuser 현재 로그인한 사용자 정보 (응답 DTO 생성 시 사용)
     * @param typeId 필터링할 커뮤니티 타입 ID (null이면 기본 타입 게시글 조회)
     * @return 페이지 정보와 게시글 목록이 담긴 CommunityListResponse DTO
     */
    // @Transactional(readOnly = true) // 클래스 레벨에 적용되어 있음
    public CommunityListResponse getPosts(Pageable pageable, LoginUser loginuser, Long typeId) {
        Page<Community> communityPage;
        Long targetTypeId = typeId; // 조회 대상 typeId를 담을 변수

        // typeId가 명시적으로 제공되지 않았거나 null인 경우
        if (targetTypeId == null) {
            // 기본값인 "개선사항 요구"의 typeId로 설정합니다.
            targetTypeId = DEFAULT_COMMUNITY_TYPE_ID;
        }
        // typeId가 제공되었거나 기본값으로 설정되었으므로 해당 타입의 게시글만 조회합니다.
        // (만약 typeId가 명시적으로 제공되었는데 그 값이 전체보기를 의미한다면,
        // 해당 값을 구분하여 communityRepository.findAll()을 호출하도록 로직 수정 필요)
        // 현재 로직은 typeId가 null이면 DEFAULT_COMMUNITY_TYPE_ID로, null이 아니면 제공된 typeId로만 조회합니다.

        communityPage = communityRepository.findAllByTypeId(targetTypeId, pageable);


        // 조회된 Page<Community>와 LoginUser 정보를 사용하여 CommunityListResponse DTO를 생성하여 반환합니다.
        return new CommunityListResponse(communityPage, loginuser);
    }

    /**
     * 사용자 권한으로 특정 커뮤니티 게시글을 수정합니다.
     * 게시글 작성자만 수정할 수 있도록 권한을 확인합니다.
     *
     * @param postId 수정할 게시글의 ID
     * @param updatedCommunityDetails 업데이트할 내용을 담은 Community 엔티티 (일부 필드만 채워져 있음)
     * @param loginuser 현재 로그인한 사용자 정보 (권한 확인 및 응답 DTO 생성 시 사용)
     * @return 수정된 게시글 정보가 담긴 CommunityResponse DTO
     */
    @Transactional // 쓰기 작업이므로 트랜잭션 적용
    public CommunityResponse updatePost(Long postId, Community updatedCommunityDetails, LoginUser loginuser) {
         Community existingCommunity = communityRepository.findById(postId)
             .orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));

        if (!existingCommunity.getAuthor().getId().equals(loginuser.getId())) {
             throw new RuntimeException("You are not authorized to update this post.");
        }

        existingCommunity.setTitle(updatedCommunityDetails.getTitle());
        existingCommunity.setContent(updatedCommunityDetails.getContent());
        existingCommunity.setType(updatedCommunityDetails.getType());
        existingCommunity.setTypeId(updatedCommunityDetails.getTypeId());

        Community updatedCommunity = communityRepository.save(existingCommunity);

        return new CommunityResponse(updatedCommunity, loginuser);
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
             throw new RuntimeException("You have already recommended this post.");
        }

        Recommendation recommendation = new Recommendation(null, user, community);

        recommendationRepository.save(recommendation);

        community.setRecommend(community.getRecommend() + 1);
        Community updatedCommunity = communityRepository.save(community);

        return new CommunityResponse(updatedCommunity, loginuser);
    }
}
