package com.backend.server.model.repository.classroom;

import com.backend.server.api.admin.classroom.dto.AdminClassroomSearchRequest;
import com.backend.server.model.entity.classroom.Classroom;
import com.backend.server.model.entity.enums.Status;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import org.springframework.data.jpa.domain.Specification;

public class ClassroomSpecification {
    public static Specification<Classroom> searchAndFilter(AdminClassroomSearchRequest request) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            String keyword = request.getKeyword();
            if (keyword != null && !keyword.trim().isEmpty()) {
                String trimmedKeyword = "%" + keyword.trim() + "%";
                switch (request.getType()) {
                    case ID:
                        if (keyword.matches("\\d+")) {
                            predicate = cb.and(predicate, cb.equal(root.get("id"), Long.parseLong(keyword)));
                        } else {
                            throw new IllegalArgumentException("ID 로 검색하기 위해선 숫자여야 합니다.");
                        }
                        break;
                    case NAME:
                        predicate = cb.and(predicate, cb.like(cb.lower(root.get("name")), trimmedKeyword.toLowerCase()));
                        break;
                    case DESCRIPTION:
                        predicate = cb.and(predicate, cb.like(cb.lower(root.get("location")), trimmedKeyword.toLowerCase()));
                        break;
                    case ALL:
                        Predicate nameMatch = cb.like(cb.lower(root.get("name")), trimmedKeyword.toLowerCase());
                        Predicate locationMatch = cb.like(cb.lower(root.get("location")), trimmedKeyword.toLowerCase());
                        Predicate idMatch = keyword.matches("\\d+")
                                ? cb.equal(root.get("id"), Long.parseLong(keyword))
                                : cb.disjunction();
                        predicate = cb.and(predicate, cb.or(nameMatch, locationMatch, idMatch));
                        break;
                }
            }

            if (request.getStatus() != null) {
                switch (request.getStatus()) {
                    case BROKEN, RENTAL_PENDING, IN_USE, AVAILABLE:
                        Status s = Status.valueOf(request.getStatus().name());
                        predicate = cb.and(predicate, cb.equal(root.get("status"), s));
                        break;
                    case CANCELABLE:
                        predicate = cb.and(predicate, cb.equal(root.get("status"), Status.IN_USE));
                        predicate = cb.and(predicate, cb.greaterThan(root.get("startTime"), LocalDateTime.now()));
                        break;
                    case ALL:
                        break;
                }
            }

            return predicate;
        };
    }
}
