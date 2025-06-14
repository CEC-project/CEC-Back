package com.backend.server.model.repository.inquiry;

import com.backend.server.api.admin.inquiry.dto.AdminInquiryListRequest;
import com.backend.server.api.admin.user.dto.AdminUserListRequest;
import com.backend.server.model.entity.Inquiry;
import com.backend.server.model.entity.User;
import com.backend.server.model.repository.user.UserSpecification;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import java.util.Optional;
import org.springframework.data.jpa.domain.Specification;

public class InquirySpecification {
    public static Specification<Inquiry> filterInquiry(AdminInquiryListRequest request) {
        return ((root, query, cb) -> {
            var predicate = cb.conjunction();

            Join<Inquiry, User> author = root.join("author", JoinType.LEFT);

            Optional<AdminUserListRequest> userListRequest = request.toAdminUserListRequest();
            if (userListRequest.isPresent())
                predicate = UserSpecification.searchAndFilterUsers(author, cb, predicate, userListRequest.get());

            String keyword = "%" + request.getSearchKeyword() + "%";
            if (request.getSearchKeyword() == null || request.getSearchKeyword().isEmpty())
                keyword = "%";

            Predicate content = cb.like(root.get("content"), keyword);

            switch (request.getSearchType()) {
                case CONTENT, ALL -> predicate = cb.and(predicate, content);
            }

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
