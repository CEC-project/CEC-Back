package com.backend.server.model.repository;

import com.backend.server.api.admin.history.dto.AdminBrokenRepairHistoryRequest;
import com.backend.server.model.entity.BrokenRepairHistory;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

public class BrokenRepairHistorySpecification {

    public static Specification<BrokenRepairHistory> filter(AdminBrokenRepairHistoryRequest request) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            // 대상 타입
            if (request.getTargetType() != AdminBrokenRepairHistoryRequest.TargetType.ALL) {
                predicate = cb.and(predicate, cb.equal(root.get("targetType"), request.getTargetType()));
            }

            // 이력 타입
            if (request.getHistoryType() != AdminBrokenRepairHistoryRequest.HistoryType.ALL) {
                predicate = cb.and(predicate, cb.equal(root.get("historyType"), request.getHistoryType()));
            }

            // 날짜 조건
            if (request.getStartDate() != null) {
                predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("createdAt"), request.getStartDate().atStartOfDay()));
            }

            if (request.getEndDate() != null) {
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("createdAt"), request.getEndDate().atTime(23, 59, 59)));
            }

            // 검색 키워드
            if (request.getSearchType() != null && request.getSearchKeyword() != null && !request.getSearchKeyword().isBlank()) {
                String keyword = "%" + request.getSearchKeyword().toLowerCase() + "%";

                switch (request.getSearchType()) {
                    case MODEL_NAME -> {
                        if (request.getTargetType() != AdminBrokenRepairHistoryRequest.TargetType.CLASSROOM) {
                            Join<Object, Object> equipment = root.join("equipment", JoinType.LEFT);
                            predicate = cb.and(predicate, cb.like(cb.lower(equipment.get("equipmentModel").get("name")), keyword));
                        }
                    }

                    case SERIAL_NUMBER -> {
                        if (request.getTargetType() != AdminBrokenRepairHistoryRequest.TargetType.CLASSROOM) {
                            Join<Object, Object> equipment = root.join("equipment", JoinType.LEFT);
                            predicate = cb.and(predicate, cb.like(cb.lower(equipment.get("serialNumber")), keyword));
                        }
                    }

                    case LOCATION -> {
                        if (request.getTargetType() != AdminBrokenRepairHistoryRequest.TargetType.EQUIPMENT) {
                            Join<Object, Object> classroom = root.join("classroom", JoinType.LEFT);
                            predicate = cb.and(predicate, cb.like(cb.lower(classroom.get("location")), keyword));
                        }
                    }

                    case BROKEN_BY_NAME -> {
                        predicate = cb.and(predicate, cb.like(cb.lower(root.get("brokenByName")), keyword));
                    }

                    case ALL -> {
                        // 전방위 검색
                        Predicate allPredicate = cb.disjunction();
                        if (request.getTargetType() != AdminBrokenRepairHistoryRequest.TargetType.CLASSROOM) {
                            Join<Object, Object> equipment = root.join("equipment", JoinType.LEFT);
                            allPredicate = cb.or(allPredicate,
                                    cb.like(cb.lower(equipment.get("equipmentModel").get("name")), keyword),
                                    cb.like(cb.lower(equipment.get("serialNumber")), keyword)
                            );
                        }
                        if (request.getTargetType() != AdminBrokenRepairHistoryRequest.TargetType.EQUIPMENT) {
                            Join<Object, Object> classroom = root.join("classroom", JoinType.LEFT);
                            allPredicate = cb.or(allPredicate,
                                    cb.like(cb.lower(classroom.get("location")), keyword)
                            );
                        }
                        allPredicate = cb.or(allPredicate, cb.like(cb.lower(root.get("brokenByName")), keyword));
                        predicate = cb.and(predicate, allPredicate);
                    }
                }
            }

            return predicate;
        };
    }
}
