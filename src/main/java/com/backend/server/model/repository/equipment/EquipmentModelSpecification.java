package com.backend.server.model.repository.equipment;

import com.backend.server.api.common.dto.PageableRequest;
import com.backend.server.api.user.equipment.dto.model.EquipmentModelListRequest;
import com.backend.server.model.entity.EquipmentModel;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class EquipmentModelSpecification {
    
   //장비 모델 필터링 전체 조회 \
    public static Specification<EquipmentModel> filterEquipmentModels(EquipmentModelListRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 검색어 필터링 (모델명, 영문코드)
            if (StringUtils.hasText(request.getKeyword())) {
                String likePattern = "%" + request.getKeyword().toLowerCase() + "%";
                
                Predicate namePredicate = cb.like(
                        cb.lower(root.get("name")), likePattern);
                Predicate englishCodePredicate = cb.like(
                        cb.lower(root.get("englishCode")), likePattern);
                
                predicates.add(cb.or(namePredicate, englishCodePredicate));
            }
            if (request.getCategoryId() != null) {
                predicates.add(cb.equal(root.get("category").get("id"), request.getCategoryId()));
            }
            
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
    //카테고리 별 조회
//    public static Specification<EquipmentModel> filterEquipmentModelsByCategory(Long categoryId) {
//        return (root, query, cb) -> {
//            if (categoryId != null) {
//                return cb.equal(root.get("category").get("id"), categoryId);
//            }
//            return cb.conjunction(); // 조건 없음 → 전체 조회
//        };
//    }

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




} 