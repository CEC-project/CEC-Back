package com.backend.server.api.admin.rentalRestriction.service;

import com.backend.server.api.admin.rentalRestriction.dto.AdminRentalRestrictionListRequest;
import com.backend.server.api.admin.rentalRestriction.dto.AdminRentalRestrictionListResponse;
import com.backend.server.api.admin.rentalRestriction.dto.AdminRentalRestrictionRequest;
import com.backend.server.api.admin.rentalRestriction.dto.AdminRentalRestrictionSortType;
import com.backend.server.api.admin.user.dto.AdminUserListResponse;
import com.backend.server.model.entity.Professor;
import com.backend.server.model.entity.RentalRestriction;
import com.backend.server.model.entity.User;
import com.backend.server.model.repository.user.RentalRestrictionRepository;
import com.backend.server.model.repository.user.RentalRestrictionSpecification;
import com.backend.server.model.repository.user.UserRepository;
import com.backend.server.model.repository.user.UserSpecification;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminRentalRestrictionService {

    private final RentalRestrictionRepository rentalRestrictionRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long addRentalRestriction(Long userId, AdminRentalRestrictionRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new IllegalArgumentException("유효하지 않은 사용자 id 입니다."));
        RentalRestriction rentalRestriction = request.toEntity(user);
        rentalRestrictionRepository.save(rentalRestriction);
        return rentalRestriction.getId();
    }

    @Transactional
    public void cancelRentalRestriction(Long id) {
        rentalRestrictionRepository.deleteById(id);
    }

    @Transactional
    public AdminRentalRestrictionListResponse getRestrictedUsers(AdminRentalRestrictionListRequest request) {
        if (request.getSortBy() == null)
            request.setSortBy(AdminRentalRestrictionSortType.getDefault());
        Pageable pageable = request.toPageable(false);

        Specification<RentalRestriction> spec = RentalRestrictionSpecification.filter(request);

        Page<RentalRestriction> page = rentalRestrictionRepository.findAll(spec, pageable);

        List<User> users = page.getContent()
                .stream()
                .map(RentalRestriction::getUser)
                .toList();

        return new AdminRentalRestrictionListResponse(page, users);
    }

    @Transactional
    public AdminUserListResponse getAllowedUsers(AdminRentalRestrictionListRequest request) {
        if (request.getSortBy() == null)
            request.setSortBy(AdminRentalRestrictionSortType.getDefault());
        Pageable pageable = request.toPageable(true);

        Specification<User> spec = UserSpecification.filterUsers(request);

        Page<User> page = userRepository.findAll(spec, pageable);
        List<Professor> professors = page.getContent()
                .stream()
                .map(User::getProfessor)
                .toList();

        return new AdminUserListResponse(page, professors);
    }
}