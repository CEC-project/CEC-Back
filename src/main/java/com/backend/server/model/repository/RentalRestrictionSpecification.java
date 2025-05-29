package com.backend.server.model.repository;

import com.backend.server.api.admin.rentalRestriction.dto.AdminRentalRestrictionListRequest;
import com.backend.server.model.entity.RentalRestriction;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import org.springframework.data.jpa.domain.Specification;

public class RentalRestrictionSpecification {

    public static Specification<RentalRestriction> filter(AdminRentalRestrictionListRequest request) {
        return (root, query, cb) -> {
            var predicate = cb.conjunction();

            query.distinct(true);
            Join<Object, Object> user = root.join("user");

            Predicate endAt = cb.greaterThanOrEqualTo(root.get("endAt"), LocalDateTime.now());
            cb.and(predicate, endAt);

            if (request.getSearchKeyword() != null && request.getSearchType() != null) {
                switch (request.getSearchType()) {
                    case 0 -> predicate = cb.and(predicate, cb.like(
                            user.get("name"), "%" + request.getSearchKeyword() + "%"));
                    case 1 -> predicate = cb.and(predicate, cb.like(
                            user.get("phoneNumber"), "%" + request.getSearchKeyword() + "%"));
                    case 2 -> predicate = cb.and(predicate, cb.like(
                            user.get("studentNumber"), "%" + request.getSearchKeyword() + "%"));
                }
            }

            if (request.getGrade() != null)
                predicate = cb.and(predicate, cb.equal(user.get("grade"), request.getGrade()));

            if (request.getGender() != null)
                predicate = cb.and(predicate, cb.equal(user.get("gender"), request.getGender()));

            if (request.getProfessorId() != null)
                predicate = cb.and(predicate, cb.equal(
                        user.join("professor", JoinType.LEFT).get("id"), request.getProfessorId()));

            if (request.getReason() != null)
                predicate = cb.and(predicate, cb.equal(root.get("reason"), request.getReason()));

            if (request.getType() != null)
                predicate = cb.and(predicate, cb.equal(user.get("type"), request.getType()));

            return predicate;
        };
    }
}
