package com.backend.server.api.admin.community.service;

import com.backend.server.api.admin.community.dto.AdminCommunityListRequest;
import com.backend.server.api.admin.community.dto.AdminCommunityListResponse;
import com.backend.server.api.admin.community.dto.AdminCommunityResponse;
import com.backend.server.model.entity.Board;
import com.backend.server.model.repository.BoardRepository;
import com.backend.server.model.repository.CommunitySpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminCommunityService {

    private final BoardRepository boardRepository;

    public AdminCommunityListResponse getCommunityPosts(AdminCommunityListRequest request) {
        Pageable pageable = request.toPageable();

        Specification<Board> spec = CommunitySpecification.filterCommunities(request);

        Page<Board> page = boardRepository.findAll(spec, pageable);

        return new AdminCommunityListResponse(page);
    }

    public AdminCommunityResponse getCommunityPost(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);

        board.increaseViewCount();

        return new AdminCommunityResponse(board);
    }

    public void deleteCommunityPost(Long id) {
        boardRepository.deleteById(id);
    }
}
