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

            predicate = UserSpecification.searchAndFilterUsers(author, cb, predicate, request.toAdminUserListRequest());

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
