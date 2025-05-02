package com.backend.server.model.repository;

import com.backend.server.api.user.dto.equipment.equipment.EquipmentModelListRequest;
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
    
    /**
     * 장비 모델 필터링을 위한 Specification 생성
     */
    public static Specification<EquipmentModel> filterEquipments(EquipmentModelListRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            // 카테고리 필터링
            if (request.getCategoryId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("categoryId"), request.getCategoryId()));
            }
            
            // 사용 가능 여부 필터링
            if (request.getAvailable() != null) {
                predicates.add(criteriaBuilder.equal(root.get("available"), request.getAvailable()));
            }
            
            // 검색어 필터링 (모델명, 영문코드)
            if (StringUtils.hasText(request.getKeyword())) {
                String likePattern = "%" + request.getKeyword().toLowerCase() + "%";
                
                Predicate namePredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("name")), likePattern);
                Predicate englishCodePredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("englishCode")), likePattern);
                
                predicates.add(criteriaBuilder.or(namePredicate, englishCodePredicate));
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
    
    /**
     * 페이징 및 정렬 정보 생성
     */
    public static Pageable getPageable(EquipmentModelListRequest request) {
        Direction direction = request.getSortDirection().equalsIgnoreCase("ASC") ? 
            Direction.ASC : Direction.DESC;
            
        return PageRequest.of(
            request.getPage(), 
            request.getSize(),
            Sort.by(direction, request.getSortBy())
        );
    }
} 