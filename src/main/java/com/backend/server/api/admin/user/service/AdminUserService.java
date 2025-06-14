package com.backend.server.api.admin.user.service;

import com.backend.server.api.admin.user.dto.AdminUserDetailListResponse;
import com.backend.server.api.admin.user.dto.AdminUserDetailResponse;
import com.backend.server.api.admin.user.dto.AdminUserListRequest;
import com.backend.server.api.admin.user.dto.AdminUserRequest;
import com.backend.server.model.entity.Professor;
import com.backend.server.model.entity.User;
import com.backend.server.model.entity.enums.Role;
import com.backend.server.model.repository.user.ProfessorRepository;
import com.backend.server.model.repository.user.UserRepository;
import com.backend.server.model.repository.user.UserSpecification;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public AdminUserDetailListResponse getUsers(AdminUserListRequest request) {
        Pageable pageable = request.toPageable();

        Specification<User> spec = UserSpecification.filterUsers(request);

        Page<User> page = userRepository.findAll(spec, pageable);
        List<Professor> professors = page.getContent()
                .stream()
                .map(User::getProfessor)
                .toList();

        return new AdminUserDetailListResponse(page, professors);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public Long updateUser(Long id, AdminUserRequest request) {
        User user = userRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        Professor professor = professorRepository.findById(request.getProfessorId())
                .orElseThrow(IllegalArgumentException::new);
        user.update(professor, request);
        user = userRepository.save(user);
        return user.getId();
    }

    @Transactional
    public Long resetUserPassword(Long id) {
        User user = userRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        user.toBuilder().password(passwordEncoder.encode(user.getStudentNumber()));
        user = userRepository.save(user);
        return user.getId();
    }

    @Transactional
    public Long createUser(AdminUserRequest request) {
        Professor professor = professorRepository.findById(request.getProfessorId())
                .orElseThrow(IllegalArgumentException::new);
        User user = request.toEntity(professor, Role.ROLE_USER, passwordEncoder);
        user = userRepository.save(user);
        return user.getId();
    }

    public List<AdminUserDetailResponse> getAdmins(List<Role> roles) {
        List<User> users = userRepository.findByRoleInOrderByNameAsc(roles);
        return users.stream()
                .map(user -> new AdminUserDetailResponse(user, user.getProfessor()))
                .toList();
    }
}