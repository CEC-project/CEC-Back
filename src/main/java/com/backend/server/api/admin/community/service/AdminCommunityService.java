package com.backend.server.api.admin.community.service;

import com.backend.server.api.common.dto.LoginUser;
import com.backend.server.api.user.community.dto.CommunityListResponse;
import com.backend.server.api.user.community.dto.CommunityResponse;
import com.backend.server.model.entity.Community;
import com.backend.server.model.repository.CommunityRepository;
import com.backend.server.api.user.community.service.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminCommunityService {

    private final CommunityRepository communityRepository;
    private final CommunityService communityService;

    // 게시글 목록 조회 (typeId 필터링 포함)
    public CommunityListResponse getCommunityPosts(Pageable pageable, LoginUser adminUser, Long typeId) {
        // adminUser 활용해 관리자 여부 전달
        return communityService.getPosts(pageable, adminUser, typeId);
    }

    // 게시글 상세 조회
    public CommunityResponse getCommunityPostById(Long id, LoginUser adminUser) {
        return communityService.getPostById(id, adminUser);
    }

    // 게시글 삭제
    public void deleteCommunityPost(Long id) {
        communityService.adminDeletePost(id);
    }

    // 게시글 수정
    public CommunityResponse updateCommunityPost(Long id, Community updatedCommunity) {
        return communityService.adminUpdatePost(id, updatedCommunity);
    }
}
