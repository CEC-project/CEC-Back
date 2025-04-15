package com.backend.server.model.repository;

import com.backend.server.api.admin.dto.user.AdminUserListRequest;
import com.backend.server.model.entity.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    public static Specification<User> filterUsers(AdminUserListRequest request) {
        return (root, query, cb) -> {
            var predicate = cb.conjunction();

            if (request.getSearchKeyword() != null && request.getSearchType() != null) {
                switch (request.getSearchType()) {
                    case 0 -> predicate = cb.and(predicate, cb.like(root.get("name"), "%" + request.getSearchKeyword() + "%"));
                    case 1 -> predicate = cb.and(predicate, cb.like(root.get("phoneNumber"), "%" + request.getSearchKeyword() + "%"));
                    case 2 -> predicate = cb.and(predicate, cb.like(root.get("studentNumber"), "%" + request.getSearchKeyword() + "%"));
                }
            }

            if (request.getYear() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("year"), request.getYear()));
            }

            if (request.getGender() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("gender"), request.getGender()));
            }

            if (request.getProfessor() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("professor"), request.getProfessor()));
            }

            return predicate;
        };
    }

    public static Pageable getPageable(AdminUserListRequest request) {
        String sortBy = switch (request.getSortBy()) {
            case 1 -> "studentNumber";
            case 2 -> "restrictionCount";
            default -> "name";
        };

        String sortDirection = request.getSortDirection();
        if (!sortDirection.toLowerCase().contentEquals("desc"))
            sortDirection = "asc";

        return PageRequest.of(
                request.getPage(),
                request.getSize(),
                Sort.by(Direction.fromString(sortDirection), sortBy)
        );
    }
}