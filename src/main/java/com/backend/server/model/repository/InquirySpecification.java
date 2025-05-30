package com.backend.server.model.repository;

import com.backend.server.api.admin.inquiry.dto.AdminInquiryListRequest;
import com.backend.server.model.entity.Inquiry;
import com.backend.server.model.entity.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class InquirySpecification {
    public static Specification<Inquiry> filterInquiry(AdminInquiryListRequest request) {
        return ((root, query, cb) -> {
            var predicate = cb.conjunction();

            Join<Inquiry, User> author = root.join("author", JoinType.LEFT);

            if (request.getSearchKeyword() != null && request.getSearchType() != null) {
                switch (request.getSearchType()) {
                    case 0 -> predicate = cb.and(predicate, cb.like(
                            author.get("name"), "%" + request.getSearchKeyword() + "%"));
                    case 1 -> predicate = cb.and(predicate, cb.like(
                            author.get("phoneNumber"), "%" + request.getSearchKeyword() + "%"));
                    case 2 -> predicate = cb.and(predicate, cb.like(
                            author.get("studentNumber"), "%" + request.getSearchKeyword() + "%"));
                }
            }

            if (request.getGrade() != null)
                predicate = cb.and(predicate, cb.equal(author.get("grade"), request.getGrade()));

            if (request.getGender() != null)
                predicate = cb.and(predicate, cb.equal(author.get("gender"), request.getGender()));

            if (request.getProfessorId() != null)
                predicate = cb.and(predicate, cb.equal(
                        author.join("professor", JoinType.LEFT).get("id"), request.getProfessorId()));

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
