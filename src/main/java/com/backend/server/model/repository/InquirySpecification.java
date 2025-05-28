package com.backend.server.model.repository;

import com.backend.server.api.admin.inquiry.dto.AdminInquiryListRequest;
import com.backend.server.model.entity.Inquiry;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class InquirySpecification {
    public static Specification<Inquiry> filterInquiry(AdminInquiryListRequest request) {
        return ((root, query, cb) -> {
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

            Join<?, ?> answers = root.join("answers", JoinType.LEFT);
            if (request.getAnswered() != null) {
                if (request.getAnswered())
                    predicate = cb.and(predicate, cb.isNotNull(answers.get("id")));
                else
                    predicate = cb.and(predicate, cb.isNull(answers.get("id")));
            }

            return predicate;
        });
    }
}
