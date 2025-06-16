package com.backend.server.api.user.history.service;

import com.backend.server.api.common.dto.LoginUser;
import com.backend.server.api.common.dto.PageableInfo;
import com.backend.server.api.user.history.dto.RentalRestrictionListRequest;
import com.backend.server.api.user.history.dto.RentalRestrictionListResponse;
import com.backend.server.api.user.history.dto.RentalRestrictionResponse;
import com.backend.server.model.entity.RentalRestriction;
import com.backend.server.model.entity.User;
import com.backend.server.model.repository.user.RentalRestrictionRepository;
import com.backend.server.model.repository.user.RentalRestrictionSpecification;
import com.backend.server.model.repository.user.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RentalRestrictionService {

    private final UserRepository userRepository;
    private final RentalRestrictionRepository rentalRestrictionRepository;

    public RentalRestrictionListResponse getRentalRestrictionList(
            RentalRestrictionListRequest request,
            LoginUser loginUser) {
        User user = userRepository.findById(loginUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("로그인 된 사용자를 DB 에서 찾을수 없습니다."));

        Specification<RentalRestriction> spec = RentalRestrictionSpecification.filter(request, user);
        Pageable pageable = request.toPageable();
        Page<RentalRestriction> page = rentalRestrictionRepository.findAll(spec, pageable);

        List<RentalRestrictionResponse> content = page.stream()
                .map(RentalRestrictionResponse::new)
                .toList();
        return new RentalRestrictionListResponse(content, new PageableInfo(page));
    }
}
