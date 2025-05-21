package com.backend.server.model.repository.equipment;

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
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
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
    //카테고리 별 조회
    public static Specification<EquipmentModel> filterEquipmentModelsByCategory(EquipmentModelListRequest request) {
        return (root, query, cb) -> {
            if (request.getCategoryId() != null) {
                return cb.equal(root.get("category").get("id"), request.getCategoryId());
            }
            return cb.conjunction(); // 조건 없음 → 전체 조회
        };
    }
    
    //페이징 생성
    //프론트에서 페이지 정보 없으면 0 , 10
    public static Pageable getPageable(EquipmentModelListRequest request) {
        Direction direction = request.getSortDirection().equalsIgnoreCase("ASC") ? 
            Direction.ASC : Direction.DESC;
        int page = request.getPage() != null ? request.getPage() : 0;
        int size = request.getSize() != null ? request.getSize() : 10;
            
        return PageRequest.of(
            page,
            size,
            Sort.by(direction, request.getSortBy())
        );
    }


} 