package com.backend.server.model.repository.user;

import com.backend.server.api.admin.rentalRestriction.dto.AdminRentalRestrictionListRequest;
import com.backend.server.model.entity.RentalRestriction;
import com.backend.server.model.entity.User;
import com.backend.server.model.repository.user.UserSpecification;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import org.springframework.data.jpa.domain.Specification;

public class RentalRestrictionSpecification {

    public static Specification<RentalRestriction> filter(AdminRentalRestrictionListRequest request) {
        return (root, query, cb) -> {
            var predicate = cb.conjunction();

            Join<RentalRestriction, User> user = root.join("user");

            Predicate endAt = cb.greaterThanOrEqualTo(root.get("endAt"), LocalDateTime.now());
            predicate = cb.and(predicate, endAt);

            predicate = UserSpecification.searchAndFilterUsers(user, cb, predicate, request.toAdminUserListRequest());

            if (request.getReason() != null)
                predicate = cb.and(predicate, cb.equal(root.get("reason"), request.getReason()));

            if (request.getType() != null)
                predicate = cb.and(predicate, cb.equal(user.get("type"), request.getType()));

            return predicate;
        };
    }
}
