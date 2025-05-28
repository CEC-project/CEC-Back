package com.backend.server.model.repository.classroom;

import com.backend.server.api.admin.classroom.dto.AdminClassroomSearchRequest;
import com.backend.server.model.entity.classroom.Classroom;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;

public class ClassroomSpecification {
    public static Specification<Classroom> searchAndOrderBy(AdminClassroomSearchRequest request) {
        return (root, query, cb) -> {
            String keyword = request.getKeyword();

            if (keyword == null || keyword.trim().isEmpty()) {
                return cb.conjunction(); // 필터 없이 전체 조회
            }

            String trimmedKeyword = "%" + keyword.trim() + "%";

            switch (request.getType()) {
                case ID:
                    if (keyword.matches("\\d+")) {
                        return cb.equal(root.get("id"), Long.parseLong(keyword));
                    } else {
                        throw new IllegalArgumentException("ID 로 검색하기 위해선 숫자여야 합니다.");
                    }
                case NAME:
                    return cb.like(cb.lower(root.get("name")), trimmedKeyword.toLowerCase());
                case DESCRIPTION:
                    return cb.like(cb.lower(root.get("location")), trimmedKeyword.toLowerCase());
                case ALL:
                    Predicate nameMatch = cb.like(cb.lower(root.get("name")), trimmedKeyword.toLowerCase());
                    Predicate locationMatch = cb.like(cb.lower(root.get("location")), trimmedKeyword.toLowerCase());
                    Predicate idMatch = keyword.matches("\\d+") ? cb.equal(root.get("id"), Long.parseLong(keyword)) : cb.disjunction();
                    return cb.or(nameMatch, locationMatch, idMatch);
                default:
                    return cb.conjunction(); // fallback
            }
        };
    }

    public static Sort getSort() {
        return Sort.by(Direction.ASC, "name", "id");
    }

    public static Sort getRequestedTimeSort() {
        return Sort.by(Direction.ASC, "requestedTime");
    }
}
