package com.backend.server.model.repository;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import com.backend.server.api.admin.equipment.dto.AdminEquipmentRentalRequestListRequest;
import com.backend.server.api.common.dto.PageableRequest;
import com.backend.server.api.user.equipment.dto.equipment.EquipmentListRequest;
import com.backend.server.api.admin.equipment.dto.AdminEquipmentListRequest;
import com.backend.server.model.entity.Equipment;
import com.backend.server.model.entity.EquipmentRental;
import com.backend.server.model.entity.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Subquery;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class EquipmentSpecification {

    // 장비 목록 필터링 (어드민용)
    public static Specification<Equipment> adminFilterEquipments(AdminEquipmentListRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
    
            // 카테고리(장비분류)
            if (request.getCategoryId() != null) {
                predicates.add(cb.equal(root.get("categoryId"), request.getCategoryId()));
            }
            // 모델명
            if (StringUtils.hasText(request.getModelName())) {
                predicates.add(cb.like(cb.lower(root.get("modelName")), "%" + request.getModelName().toLowerCase() + "%"));
            }
            // 일련번호
            if (StringUtils.hasText(request.getSerialNumber())) {
                predicates.add(cb.like(cb.lower(root.get("serialNumber")), "%" + request.getSerialNumber().toLowerCase() + "%"));
            }
            // 장비 상태
            if (StringUtils.hasText(request.getStatus())) {
                predicates.add(cb.equal(root.get("rentalStatus"), request.getStatus()));
            }
            // 대여 가능 여부
            if (request.getIsAvailable() != null) {
                predicates.add(cb.equal(root.get("available"), request.getIsAvailable()));
            }
            // 현재 대여자 이름 (join 필요)
            if (StringUtils.hasText(request.getRenterName())) {
                Join<Equipment, User> renter = root.join("renter", JoinType.LEFT);

                predicates.add(cb.and(
                        cb.isNotNull(renter.get("name")),  // 핵심 안전 처리
                        cb.like(cb.lower(renter.get("name")), "%" + request.getRenterName().toLowerCase() + "%")
                ));
            }
            if (StringUtils.hasText(request.getSearchKeyword())) {
                String keyword = "%" + request.getSearchKeyword().toLowerCase() + "%";

                Predicate modelNamePredicate = cb.like(cb.lower(root.get("modelName")), keyword);
                Predicate serialNumberPredicate = cb.like(cb.lower(root.get("serialNumber")), keyword);

                Join<Equipment, User> renter = root.join("renter", JoinType.LEFT);

                Predicate renterNamePredicate = cb.and(
                        cb.isNotNull(renter.get("name")),  // 반드시 null 체크!
                        cb.like(cb.lower(renter.get("name")), keyword)
                );

                predicates.add(cb.or(modelNamePredicate, serialNumberPredicate, renterNamePredicate));
            }
    
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
    //어드민 장비 목록 페이지네이션
    public static Pageable getPageable(AdminEquipmentListRequest request) {
        String sortBy = request.getSortBy() != null ? request.getSortBy() : "id";
        String sortDirection = request.getSortDirection() != null ? request.getSortDirection() : "asc";
        int page = request.getPage() != null ? request.getPage() : 0;
        int size = request.getSize() != null ? request.getSize() : 17;
        return PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDirection), sortBy));
    }
    
    //장비 필터링 유저용 
    public static Specification<Equipment> filterEquipments(EquipmentListRequest request, Integer userGrade) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
    
            // 카테고리(장비분류)
            if (request.getCategoryId() != null) {
                predicates.add(cb.equal(root.get("categoryId"), request.getCategoryId()));
            }
            // 모델명
            if (StringUtils.hasText(request.getModelName())) {
                predicates.add(cb.like(cb.lower(root.get("modelName")), "%" + request.getModelName().toLowerCase() + "%"));
            }
            // 대여 가능 여부
            if (request.getIsAvailable() != null) {
                predicates.add(cb.equal(root.get("available"), request.getIsAvailable()));
            }
            // 현재 대여자 이름 (join 필요)
            if (StringUtils.hasText(request.getRenterName())) {
                Join<Equipment, User> renter = root.join("renter", JoinType.LEFT);

                predicates.add(cb.and(
                        cb.isNotNull(renter.get("name")),  // 핵심 안전 처리
                        cb.like(cb.lower(renter.get("name")), "%" + request.getRenterName().toLowerCase() + "%")
                ));
            }

            if (StringUtils.hasText(request.getSearchKeyword())) {
                String keyword = "%" + request.getSearchKeyword().toLowerCase() + "%";

                Predicate modelNamePredicate = cb.like(cb.lower(root.get("modelName")), keyword);
                Predicate serialNumberPredicate = cb.like(cb.lower(root.get("serialNumber")), keyword);

                Join<Equipment, User> renter = root.join("renter", JoinType.LEFT);

                Predicate renterNamePredicate = cb.and(
                        cb.isNotNull(renter.get("name")),  // 반드시 null 체크!
                        cb.like(cb.lower(renter.get("name")), keyword)
                );

                predicates.add(cb.or(modelNamePredicate, serialNumberPredicate, renterNamePredicate));
            }

            // 대여 제한 학년 필터링 - 유저의 학년이 대여제한학년에 포함되지 않는 장비만 표시
            if (userGrade != null) {
                predicates.add(cb.not(cb.like(root.get("restrictionGrade"), "%" + userGrade + "%")));
            }
    
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
} 