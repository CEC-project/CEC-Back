package com.backend.server.model.repository;

import com.backend.server.api.admin.dto.classroom.AdminClassRoomRentalRequestListRequest;
import com.backend.server.api.common.dto.classRoom.CommonClassRoomListRequest;
import com.backend.server.model.entity.ClassRoom;
import com.backend.server.model.entity.ClassRoomRental;
import com.backend.server.model.entity.enums.Status;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

import jakarta.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ClassRoomSpecification {
    
    public static Specification<ClassRoom> filterClassRooms(CommonClassRoomListRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 상태 필터링
            if (request.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), Status.valueOf(request.getStatus())));
            }

            // 대여 가능 여부 필터링
            if (request.getAvailable() != null) {
                LocalDateTime now = LocalDateTime.now();
                if (request.getAvailable()) {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("availableStartTime"), now));
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("availableEndTime"), now));
                } else {
                    predicates.add(criteriaBuilder.or(
                        criteriaBuilder.greaterThan(root.get("availableStartTime"), now),
                        criteriaBuilder.lessThan(root.get("availableEndTime"), now)
                    ));
                }
            }

            // 검색어 필터링
            if (request.getSearchKeyword() != null && !request.getSearchKeyword().isEmpty()) {
                if (request.getSearchType() == 0) { // 강의실 이름으로 검색
                    predicates.add(criteriaBuilder.like(root.get("name"), "%" + request.getSearchKeyword() + "%"));
                } else if (request.getSearchType() == 1) { // 위치로 검색
                    predicates.add(criteriaBuilder.like(root.get("location"), "%" + request.getSearchKeyword() + "%"));
                }
            }

            // 즐겨찾기 필터링
            if (request.getFavoriteOnly() != null && request.getFavoriteOnly()) {
                predicates.add(criteriaBuilder.isTrue(root.get("favorite")));
            }

            // 정렬 조건 추가
            if (request.getSortBy() != null && request.getSortDirection() != null) {
                Sort.Direction direction = Sort.Direction.fromString(request.getSortDirection());
                if (direction.isAscending()) {
                    query.orderBy(criteriaBuilder.asc(root.get(request.getSortBy())));
                } else {
                    query.orderBy(criteriaBuilder.desc(root.get(request.getSortBy())));
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<ClassRoomRental> filterRentalRequests(AdminClassRoomRentalRequestListRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            // 대여 상태 필터링
            if (StringUtils.hasText(request.getStatus())) {
                predicates.add(criteriaBuilder.equal(root.get("available"), request.getStatus()));
            }
            
            // 검색어 필터링 (장비 이름, 모델명)
            if (StringUtils.hasText(request.getSearchKeyword())) {
                String keyword = "%" + request.getSearchKeyword().toLowerCase() + "%";
                Integer searchType = request.getSearchType();
                
                if (searchType != null) {
                    if (searchType == 0) {
                        // 사용자 이름 검색
                        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.join("user").get("name")), keyword));
                    } else if (searchType == 1) {
                        // 장비 이름 검색
                        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.join("classRoom").get("name")), keyword));
                    } else if (searchType == 2) {
                        // 위치 검색
                        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.join("classRoom").get("location")), keyword));
                    }
                } else {
                    // 기본적으로 모든 필드 검색
                    Predicate userNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.join("user").get("name")), keyword);
                    Predicate equipmentNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.join("classRoom").get("name")), keyword);
                    Predicate categoryPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.join("classRoom").get("location")), keyword);
                    predicates.add(criteriaBuilder.or(userNamePredicate, equipmentNamePredicate, categoryPredicate));
                }
            }
            
            // 날짜 범위 필터링
            if (request.getStartDate() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("rentalTime"), request.getStartDate()));
            }
            
            if (request.getEndDate() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("rentalTime"), request.getEndDate()));
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
    

    public static Pageable getPageable(CommonClassRoomListRequest request) {
        String sortBy = switch (request.getSortBy()) {
            case "location" -> "location";
            default -> "name";
        };

        String sortDirection = request.getSortDirection();
        if (!sortDirection.toLowerCase().contentEquals("desc"))
            sortDirection = "asc";

        return PageRequest.of(
                request.getPage(),
                request.getSize(),
                Sort.by(Direction.fromString(sortDirection), sortBy)
        );
    }

    public static Pageable getPageable(AdminClassRoomRentalRequestListRequest request) {
        String sortBy = switch (request.getSortBy()) {
            case "location" -> "location";
            default -> "name";
        };

        String sortDirection = request.getSortDirection();
        if (!sortDirection.toLowerCase().contentEquals("desc"))
            sortDirection = "asc";

        return PageRequest.of(
                request.getPage(),
                request.getSize(),
                Sort.by(Direction.fromString(sortDirection), sortBy)
        );
    }
}
