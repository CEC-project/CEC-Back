package com.backend.server.model.repository.equipment;
import com.backend.server.model.entity.enums.Status;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import com.backend.server.api.common.dto.PageableRequest;
import com.backend.server.api.user.equipment.dto.equipment.EquipmentListRequest;
import com.backend.server.api.admin.equipment.dto.equipment.request.AdminEquipmentListRequest;
import com.backend.server.model.entity.Equipment;
import com.backend.server.model.entity.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;

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

            // 카테고리(장비분류)
            if (request.getCategoryId() != null) {
                predicates.add(cb.equal(root.get("equipmentCategory").get("id"), request.getCategoryId()));

            }

            // 모델명
            if (StringUtils.hasText(request.getModelName())) {

                predicates.add(cb.like(cb.lower(root.get("equipmentModel").get("name")), "%" + request.getModelName().toLowerCase() + "%"));

            }

            // 일련번호
            if (StringUtils.hasText(request.getSerialNumber())) {
                predicates.add(cb.like(cb.lower(root.get("serialNumber")), "%" + request.getSerialNumber().toLowerCase() + "%"));
            }

            // 장비 상태
            if (StringUtils.hasText(request.getStatus())) {
                Status statusEnum = Status.valueOf(request.getStatus().toUpperCase());
                predicates.add(cb.equal(root.get("status"), statusEnum));
            }


            // 현재 대여자 이름
            Join<Equipment, User> renter = root.join("renter", JoinType.LEFT);
            if (StringUtils.hasText(request.getRenterName())) {
                predicates.add(cb.and(
                        cb.isNotNull(renter.get("name")),
                        cb.like(cb.lower(renter.get("name")), "%" + request.getRenterName().toLowerCase() + "%")
                ));
            }

            // 키워드 검색
            if (StringUtils.hasText(request.getSearchKeyword())) {
                String keyword = "%" + request.getSearchKeyword().toLowerCase() + "%";

                Predicate modelNamePredicate = cb.like(cb.lower(root.get("equipmentModel").get("name")), keyword);
                Predicate serialNumberPredicate = cb.like(cb.lower(root.get("serialNumber")), keyword);
                Predicate renterNamePredicate = cb.and(
                        cb.isNotNull(renter.get("name")),
                        cb.like(cb.lower(renter.get("name")), keyword)
                );

                predicates.add(cb.or(modelNamePredicate, serialNumberPredicate, renterNamePredicate));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Equipment> filterEquipments(EquipmentListRequest request, Integer userGrade) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 카테고리(장비분류)
            if (request.getCategoryId() != null) {

                predicates.add(cb.equal(root.get("equipmentCategory").get("id"), request.getCategoryId()));

            }

            // 모델명
            if (StringUtils.hasText(request.getModelName())) {
                predicates.add(cb.like(cb.lower(root.get("equipmentModel").get("name")), "%" + request.getModelName().toLowerCase() + "%"));
            }

            // 장비 상태
            if (StringUtils.hasText(request.getStatus())) {
                Status statusEnum = Status.valueOf(request.getStatus().toUpperCase());
                predicates.add(cb.equal(root.get("status"), statusEnum));
            }

            // 현재 대여자 이름
            Join<Equipment, User> renter = root.join("renter", JoinType.LEFT);
            if (StringUtils.hasText(request.getRenterName())) {
                predicates.add(cb.and(
                        cb.isNotNull(renter.get("name")),
                        cb.like(cb.lower(renter.get("name")), "%" + request.getRenterName().toLowerCase() + "%")
                ));
            }

            // 키워드 검색
            if (StringUtils.hasText(request.getSearchKeyword())) {
                String keyword = "%" + request.getSearchKeyword().toLowerCase() + "%";

                Predicate modelNamePredicate = cb.like(cb.lower(root.get("equipmentModel").get("name")), keyword);
                Predicate serialNumberPredicate = cb.like(cb.lower(root.get("serialNumber")), keyword);
                Predicate renterNamePredicate = cb.and(
                        cb.isNotNull(renter.get("name")),
                        cb.like(cb.lower(renter.get("name")), keyword)
                );

                predicates.add(cb.or(modelNamePredicate, serialNumberPredicate, renterNamePredicate));
            }

            // 대여 제한 학년 필터링
            if (userGrade != null) {
                predicates.add(cb.not(cb.like(root.get("restrictionGrade"), "%" + userGrade + "%")));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
} 