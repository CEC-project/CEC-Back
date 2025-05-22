package com.backend.server.model.repository;

import com.backend.server.api.admin.rentalRestriction.dto.AdminRentalRestrictionListRequest;
import com.backend.server.api.admin.user.dto.AdminUserListRequest;
import com.backend.server.model.entity.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDateTime;
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

            if (request.getGrade() != null)
                predicate = cb.and(predicate, cb.equal(root.get("grade"), request.getGrade()));

            if (request.getGender() != null)
                predicate = cb.and(predicate, cb.equal(root.get("gender"), request.getGender()));

            if (request.getProfessorId() != null)
                predicate = cb.and(predicate, cb.equal(
                        root.join("professor", JoinType.LEFT).get("id"), request.getProfessorId()));

            return predicate;
        };
    }

    public static Specification<User> filterUsers(AdminRentalRestrictionListRequest request, boolean restricted) {
        return (root, query, cb) -> {
            var predicate = cb.conjunction();

            query.distinct(true);
            Join<Object, Object> rentalRestrictions = root.join("rentalRestrictions");

            Predicate endAt = cb.greaterThanOrEqualTo(rentalRestrictions.get("endAt"), LocalDateTime.now());
            if (!restricted)
                endAt = endAt.not();
            cb.and(predicate, endAt);

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

            if (request.getGrade() != null)
                predicate = cb.and(predicate, cb.equal(root.get("grade"), request.getGrade()));

            if (request.getGender() != null)
                predicate = cb.and(predicate, cb.equal(root.get("gender"), request.getGender()));

            if (request.getProfessorId() != null)
                predicate = cb.and(predicate, cb.equal(
                        root.join("professor", JoinType.LEFT).get("id"), request.getProfessorId()));

            if (request.getReason() != null)
                predicate = cb.and(predicate, cb.equal(rentalRestrictions.get("reason"), request.getReason()));

            if (request.getType() != null)
                predicate = cb.and(predicate, cb.equal(root.get("type"), request.getType()));

            return predicate;
        };
    }
}