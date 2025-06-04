package com.backend.server.model.repository.equipment;
import com.backend.server.model.entity.enums.Status;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import com.backend.server.api.common.dto.PageableRequest;
import com.backend.server.api.user.equipment.dto.equipment.EquipmentListRequest;
import com.backend.server.api.admin.equipment.dto.equipment.request.AdminEquipmentListRequest;
import com.backend.server.model.entity.equipment.Equipment;
import com.backend.server.model.entity.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import jakarta.persistence.criteria.Predicate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.backend.server.api.admin.classroom.dto.AdminClassroomSearchRequest.Status.CANCELABLE;

public class EquipmentSpecification {

    public static <T extends PageableRequest> Pageable getPageable(T request) {
        int page = request.getPage() != null ? request.getPage() : 0;
        int size = request.getSize() != null ? request.getSize() : 17;
        String sortBy = StringUtils.hasText(request.getSortBy()) ? request.getSortBy() : "id";

        String rawDirection = StringUtils.hasText(request.getSortDirection())
                ? request.getSortDirection()
                : "DESC";

        // 안전하게 enum으로 변환. 잘못된 값이면 DESC로 폴백
        Sort.Direction direction;
        try {
            direction = Sort.Direction.fromString(rawDirection);
        } catch (IllegalArgumentException ex) {
            direction = Sort.Direction.DESC;
        }

        return PageRequest.of(page, size, Sort.by(direction, sortBy));
    }

    public static Specification<Equipment> adminFilterEquipments(AdminEquipmentListRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 카테고리 필터
            if (request.getCategoryId() != null) {
                predicates.add(cb.equal(root.get("equipmentCategory").get("id"), request.getCategoryId()));
            }

            // 상태 필터
            if (request.getStatus() != null) {
                switch (request.getStatus()) {
                    case BROKEN, RENTAL_PENDING, IN_USE, AVAILABLE -> {
                        Status s = Status.valueOf(request.getStatus().name());
                        predicates.add(cb.equal(root.get("status"), s));
                    }
                    case CANCELABLE -> {
                        predicates.add(cb.equal(root.get("status"), Status.IN_USE));
                        predicates.add(cb.greaterThan(root.get("startTime"), LocalDateTime.now()));
                    }
                    case ALL -> {
                        // 조건 없음
                    }
                }
            }

            // 검색 키워드 필터
            if (StringUtils.hasText(request.getSearchKeyword())) {
                String keyword = "%" + request.getSearchKeyword().toLowerCase() + "%";
                AdminEquipmentListRequest.SearchType type = request.getSearchType() != null
                        ? request.getSearchType()
                        : AdminEquipmentListRequest.SearchType.ALL;

                Join<Equipment, User> renter = root.join("renter", JoinType.LEFT);

                switch (type) {
                    case MODEL_NAME -> predicates.add(
                            cb.like(cb.lower(root.get("equipmentModel").get("name")), keyword));
                    case SERIAL_NUMBER -> predicates.add(
                            cb.like(cb.lower(root.get("serialNumber")), keyword));
                    case RENTER_NAME -> predicates.add(cb.and(
                            cb.isNotNull(renter.get("name")),
                            cb.like(cb.lower(renter.get("name")), keyword)));
                    case CATEGORY_NAME -> predicates.add(cb.like(
                            cb.lower(root.get("equipmentCategory").get("name")), keyword));
                    case ALL -> {
                        Predicate modelNamePredicate = cb.like(cb.lower(root.get("equipmentModel").get("name")), keyword);
                        Predicate serialNumberPredicate = cb.like(cb.lower(root.get("serialNumber")), keyword);
                        Predicate renterNamePredicate = cb.and(
                                cb.isNotNull(renter.get("name")),
                                cb.like(cb.lower(renter.get("name")), keyword));
                        Predicate categoryNamePredicate = cb.like(cb.lower(root.get("equipmentCategory").get("name")), keyword);

                        predicates.add(cb.or(modelNamePredicate, serialNumberPredicate, renterNamePredicate, categoryNamePredicate));
                    }
                }
            }



            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }


    public static Specification<Equipment> filterEquipments(EquipmentListRequest request, Integer userGrade) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 카테고리 필터
            if (request.getCategoryId() != null) {
                predicates.add(cb.equal(root.get("equipmentCategory").get("id"), request.getCategoryId()));
            }

            String keyword = request.getKeyword();
            EquipmentListRequest.SearchType searchType = request.getSearchType() != null
                    ? request.getSearchType()
                    : EquipmentListRequest.SearchType.ALL;

            if (StringUtils.hasText(keyword)) {
                String likeKeyword = "%" + keyword.toLowerCase() + "%";

                switch (searchType) {
                    case MODEL_NAME -> predicates.add(cb.like(cb.lower(root.get("equipmentModel").get("name")), likeKeyword));
                    case CATEGORY_NAME -> predicates.add(cb.like(cb.lower(root.get("equipmentCategory").get("name")), likeKeyword));
                    case RENTER_NAME -> {
                        Join<Equipment, User> renter = root.join("renter", JoinType.LEFT);
                        predicates.add(cb.and(
                                cb.isNotNull(renter.get("name")),
                                cb.like(cb.lower(renter.get("name")), likeKeyword)
                        ));
                    }
                    case ALL -> {
                        List<Predicate> keywordPredicates = new ArrayList<>();
                        keywordPredicates.add(cb.like(cb.lower(root.get("equipmentModel").get("name")), likeKeyword));
                        keywordPredicates.add(cb.like(cb.lower(root.get("equipmentCategory").get("name")), likeKeyword));
                        Join<Equipment, User> renter = root.join("renter", JoinType.LEFT);
                        keywordPredicates.add(cb.and(
                                cb.isNotNull(renter.get("name")),
                                cb.like(cb.lower(renter.get("name")), likeKeyword)
                        ));
                        predicates.add(cb.or(keywordPredicates.toArray(new Predicate[0])));
                    }
                }
            }

            // 상태 필터
            if (request.getStatus() != null) {
                switch (request.getStatus()) {
                    case BROKEN, RENTAL_PENDING, IN_USE, AVAILABLE -> {
                        Status s = Status.valueOf(request.getStatus().name());
                        predicates.add(cb.equal(root.get("status"), s));
                    }
                    case ALL -> {
                        // 조건 없음
                    }
                }
            }

            // 대여 제한 학년이 걸리는 장비는 조회 제외
            if (userGrade != null) {
                predicates.add(cb.not(cb.like(root.get("restrictionGrade"), "%" + userGrade + "%")));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }


} 