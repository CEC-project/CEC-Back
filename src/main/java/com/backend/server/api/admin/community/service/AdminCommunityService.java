package com.backend.server.api.admin.community.service;

import com.backend.server.api.admin.community.dto.AdminCommunityListRequest;
import com.backend.server.api.admin.community.dto.AdminCommunityListResponse;
import com.backend.server.api.admin.community.dto.AdminCommunityResponse;
import com.backend.server.model.entity.Community;
import com.backend.server.model.repository.CommunityRepository;
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

    private final CommunityRepository communityRepository;

    public AdminCommunityListResponse getCommunityPosts(AdminCommunityListRequest request) {
        Pageable pageable = request.toPageable();

        Specification<Community> spec = CommunitySpecification.filterCommunities(request);

        Page<Community> page = communityRepository.findAll(spec, pageable);

        return new AdminCommunityListResponse(page);
    }

    public AdminCommunityResponse getCommunityPost(Long id) {
        Community community = communityRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);

        return new AdminCommunityResponse(community);
    }

    public void deleteCommunityPost(Long id) {
        communityRepository.deleteById(id);
    }
}
