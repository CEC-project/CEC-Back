package com.backend.server.model.repository;

import com.backend.server.api.admin.user.dto.AdminUserListRequest;
import com.backend.server.api.admin.user.dto.AdminUserSortType;
import com.backend.server.model.entity.User;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    public static Specification<User> filterUsers(AdminUserListRequest request) {
        return (root, query, cb) -> {
            var predicate = cb.conjunction();

            if (request.getSearchKeyword() != null && request.getSearchType() != null) {
                switch (request.getSearchType()) {
                    case 0 -> predicate = cb.and(predicate, cb.like(
                            root.get("name"), "%" + request.getSearchKeyword() + "%"));
                    case 1 -> predicate = cb.and(predicate, cb.like(
                            root.get("phoneNumber"), "%" + request.getSearchKeyword() + "%"));
                    case 2 -> predicate = cb.and(predicate, cb.like(
                            root.get("studentNumber"), "%" + request.getSearchKeyword() + "%"));
                }
            }

            if (request.getGrade() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("grade"), request.getGrade()));
            }

            if (request.getGender() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("gender"), request.getGender()));
            }

            if (request.getProfessorId() != null) {
                predicate = cb.and(predicate, cb.equal(
                        root.join("professor", JoinType.LEFT).get("id"), request.getProfessorId()));
            }

            return predicate;
        };
    }

    public static Pageable getPageable(AdminUserListRequest request) {
        boolean isSortByNull = request.getSortBy() == null;

        return PageRequest.of(
                request.getPage(),
                request.getSize(),
                request.getDirection(),
                isSortByNull ? AdminUserSortType.getDefault().getField() : request.getSortBy().getField()
        );
    }
}