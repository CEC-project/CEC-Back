package com.backend.server.api.admin.user.service;

import com.backend.server.api.admin.user.dto.AdminUserListRequest;
import com.backend.server.api.admin.user.dto.AdminUserListResponse;
import com.backend.server.api.admin.user.dto.AdminUserRequest;
import com.backend.server.api.admin.user.dto.AdminUserResponse;
import com.backend.server.model.entity.Professor;
import com.backend.server.model.entity.User;
import com.backend.server.model.entity.enums.Role;
import com.backend.server.model.repository.UserRepository;
import com.backend.server.model.repository.UserSpecification;
import com.backend.server.model.repository.ProfessorRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;
    private final ProfessorRepository professorRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminUserListResponse getUsers(AdminUserListRequest request) {
        Pageable pageable = UserSpecification.getPageable(request);
        Specification<User> spec = UserSpecification.filterUsers(request);

        Page<User> page = userRepository.findAll(spec, pageable);

        return new AdminUserListResponse(page);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public AdminUserResponse updateUser(Long id, AdminUserRequest request) {
        User user = userRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        Professor professor = professorRepository.findById(request.getProfessorId())
                .orElseThrow(IllegalArgumentException::new);
        user.update(professor, request);
        user = userRepository.save(user);
        return new AdminUserResponse(user);
    }

    @Transactional
    public AdminUserResponse resetUserPassword(Long id) {
        User user = userRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        user.toBuilder().password(passwordEncoder.encode(user.getStudentNumber()));
        userRepository.save(user);
        return new AdminUserResponse(user);
    }

    @Transactional
    public AdminUserResponse createUser(AdminUserRequest request) {
        Professor professor = professorRepository.findById(request.getProfessorId())
                .orElseThrow(IllegalArgumentException::new);
        User user = request.toEntity(professor, Role.ROLE_USER, passwordEncoder);
        user = userRepository.save(user);
        return new AdminUserResponse(user);
    }

    public List<AdminUserResponse> getAdmins() {
        return userRepository.findByRoleInOrderByNameAsc(List.of(Role.ROLE_ADMIN, Role.ROLE_SUPER_ADMIN))
                .stream()
                .map(AdminUserResponse::new)
                .collect(Collectors.toList());
    }
}