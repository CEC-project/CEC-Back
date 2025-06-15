package com.backend.server.api.user.board.service;

import com.backend.server.api.admin.community.dto.CommunityListRequest;
import com.backend.server.api.common.dto.BoardResponse;
import com.backend.server.api.common.dto.LoginUser;
import com.backend.server.api.user.board.dto.PostListResponse;
import com.backend.server.api.user.board.dto.PostRequest;
import com.backend.server.api.user.board.dto.PostResponse;
import com.backend.server.api.user.board.dto.UpdatePostRequest;
import com.backend.server.model.entity.Board;
import com.backend.server.model.entity.BoardCategory;
import com.backend.server.model.entity.Recommendation;
import com.backend.server.model.entity.User;
import com.backend.server.model.repository.board.BoardCategoryRepository;
import com.backend.server.model.repository.board.BoardRepository;
import com.backend.server.model.repository.board.CommunitySpecification;
import com.backend.server.model.repository.board.RecommendationRepository;
import com.backend.server.model.repository.user.UserRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 커뮤니티 게시글과 관련된 비즈니스 로직을 처리하는 서비스 클래스입니다.
 * 게시글 생성, 조회, 수정, 삭제, 추천 등의 기능을 수행합니다.
 */
@Service
@Transactional(readOnly = true) // 읽기 전용 메소드에 대한 트랜잭션 성능 최적화
@RequiredArgsConstructor

public class BoardService {
    private final BoardRepository boardRepository; // Community 엔티티 데이터 접근을 위한 Repository
    private final UserRepository userRepository; // User 엔티티 데이터 접근을 위한 Repository (작성자 정보 조회 시 필요)
    private final RecommendationRepository recommendationRepository; // Recommendation 엔티티 데이터 접근을 위한 Repository (추천 기능 시 필요)
    private final BoardCategoryRepository boardCategoryRepository;

    public boolean hasUserRecommended(Long postId, Long userId) {
        // RecommendationRepository의 existsByUserIdAndCommunityId 메소드를 호출하여 추천 기록 존재 여부를 확인
        return recommendationRepository.existsByUserIdAndBoardId(userId, postId);
    }

    /**
     * 새로운 커뮤니티 게시글을 생성합니다.
     *
     * @param authorId 게시글 작성자의 ID
     * @param loginuser 현재 로그인한 사용자 정보 (응답 DTO 생성 시 필요)
     * @return 생성된 게시글 정보가 담긴 CommunityResponse DTO
     */
    @Transactional // 쓰기 작업이므로 트랜잭션 적용
    public PostResponse createPost(PostRequest request, Long authorId, LoginUser loginuser) {
        User author = userRepository.findById(authorId)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + authorId));
        BoardCategory category = boardCategoryRepository.findById(request.getCategoryId()).orElseThrow(()->new IllegalArgumentException("카테고리 없음"));
        Board board = Board.builder()
            .title(request.getTitle())
            .content(request.getContent())
            .boardCategory(category)
            .author(author)
            .nickname(author.getNickname())
            .recommend(0)
            .view(0)
            .build();

        Board savedBoard = boardRepository.save(board);

        return new PostResponse(savedBoard);
    }

    @Transactional(readOnly = true)
    public List<BoardResponse> getBoardCategories() {
        List<BoardCategory> categories = boardCategoryRepository.findAll();
        return categories.stream()
                .map(BoardResponse::from)
                .sorted(Comparator.comparing(BoardResponse::id))
                .collect(Collectors.toList());
    }
    /**
     * 특정 ID를 가진 커뮤니티 게시글의 상세 정보를 조회합니다.
     * 게시글 조회 시 조회수를 1 증가시킵니다.
     *
     * @param postId 조회할 게시글의 ID
     * @return 조회된 게시글 정보가 담긴 CommunityResponse DTO
     */
    @Transactional // 조회수 증가(쓰기 작업)가 포함되므로 트랜잭션 적용
    public PostResponse getPostById(Long postId) {
        Optional<Board> communityOptional = boardRepository.findById(postId);

        Board board = communityOptional.orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));

        board.setView(board.getView() + 1);
        Board viewedBoard = boardRepository.save(board);

        return new PostResponse(viewedBoard);
    }


    // @Transactional(readOnly = true) // 클래스 레벨에 적용되어 있음
    public PostListResponse getPosts(CommunityListRequest request) {
        Pageable pageable = request.toPageable();

        Specification<Board> spec = CommunitySpecification.filterCommunitiesForUser(request);

        Page<Board> page = boardRepository.findAll(spec, pageable);

        return new PostListResponse(page);
    }

    @Transactional
    public PostResponse updatePost(Long postId,
                                    UpdatePostRequest request,
                                    LoginUser loginUser) {
        Board existing = boardRepository.findById(postId)
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
        Board updated = boardRepository.save(existing);

        return new PostResponse(updated);
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
        Board existingBoard = boardRepository.findById(postId)
             .orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));

        if (!existingBoard.getAuthor().getId().equals(loginuser.getId())) {
            throw new RuntimeException("You are not authorized to delete this post.");
        }

        boardRepository.deleteById(postId);
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
    public PostResponse recommendPost(Long postId, LoginUser loginuser) {
        Board board = boardRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));

        User user = userRepository.findById(loginuser.getId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + loginuser.getId()));

        if (recommendationRepository.existsByUserIdAndBoardId(user.getId(), board.getId())) {
            Recommendation recommendation = recommendationRepository.findByUserAndBoard(user, board);

            recommendationRepository.delete(recommendation);

            board.setRecommend(board.getRecommend() - 1);
            Board updatedBoard = boardRepository.save(board);
            return new PostResponse(updatedBoard);
        } //1번 더 클릭 시 감소로 변경

        Recommendation recommendation = new Recommendation(null, user, board);

        recommendationRepository.save(recommendation);

        board.setRecommend(board.getRecommend() + 1);
        Board updatedBoard = boardRepository.save(board);

        return new PostResponse(updatedBoard);
    }
}
