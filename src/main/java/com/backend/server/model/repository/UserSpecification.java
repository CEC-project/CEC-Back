package com.backend.server.model.repository;

import com.backend.server.api.admin.rentalRestriction.dto.AdminRentalRestrictionListRequest;
import com.backend.server.api.admin.user.dto.AdminUserListRequest;
import com.backend.server.model.entity.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    public static Specification<User> filterUsers(AdminUserListRequest request) {
        return (root, query, cb) -> {
            var predicate = cb.conjunction();

            predicate = searchAndFilterUsers(root, cb, predicate, request);

            return predicate;
        };
    }

    public static Specification<User> filterUsers(AdminRentalRestrictionListRequest request) {
        return (root, query, cb) -> {
            var predicate = cb.conjunction();

            query.distinct(true);
            Join<Object, Object> rentalRestrictions = root.join("rentalRestrictions", JoinType.LEFT);

            Predicate endAt = cb.lessThan(rentalRestrictions.get("endAt"), LocalDateTime.now());
            Predicate isNull = cb.isNull(rentalRestrictions);
            predicate = cb.and(predicate, cb.or(isNull, endAt));

            predicate = searchAndFilterUsers(root, cb, predicate, request.toAdminUserListRequest());

            if (request.getReason() != null)
                predicate = cb.and(predicate, cb.equal(rentalRestrictions.get("reason"), request.getReason()));

            if (request.getType() != null)
                predicate = cb.and(predicate, cb.equal(root.get("type"), request.getType()));

            return predicate;
        };
    }

    public static Predicate searchAndFilterUsers(
            From<?, User> root, CriteriaBuilder cb, Predicate predicate, AdminUserListRequest request) {

        // 이름, 전번, 학번, 닉네임 으로 검색합니다.
        String keyword = "%" + request.getSearchKeyword() + "%";
        if (request.getSearchKeyword() == null || request.getSearchKeyword().isEmpty())
            keyword = "%";

        Predicate name = cb.like(root.get("name"), keyword);
        Predicate phoneNumber = cb.like(root.get("phoneNumber"), keyword);
        Predicate studentNumber = cb.like(root.get("studentNumber"), keyword);
        Predicate nickname = cb.like(root.get("nickname"), keyword);
        Predicate all = cb.or(name, phoneNumber, studentNumber, nickname);

        switch (request.getSearchType()) {
            case NAME -> predicate = cb.and(predicate, name);
            case PHONE_NUMBER -> predicate = cb.and(predicate, phoneNumber);
            case STUDENT_NUMBER -> predicate = cb.and(predicate, studentNumber);
            case NICKNAME -> predicate = cb.and(predicate, nickname);
            case ALL -> predicate = cb.and(predicate, all);
        }

        // 필터링 합니다.
        if (request.getGrade() != null)
            predicate = cb.and(predicate, cb.equal(root.get("grade"), request.getGrade()));

        if (request.getGender() != null)
            predicate = cb.and(predicate, cb.equal(root.get("gender"), request.getGender().name()));

        if (request.getProfessorId() != null)
            predicate = cb.and(predicate, cb.equal(
                    root.join("professor", JoinType.LEFT).get("id"), request.getProfessorId()));

        return predicate;
    }
}