package com.backend.server.model.repository;

import com.backend.server.api.user.dto.EquipmentListRequest;
import com.backend.server.model.entity.Equipment;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class EquipmentSpecification {
    
    // 장비 목록 필터링 (어드민용)
    public static Specification<Equipment> AdminfilterEquipments(EquipmentListRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            // 카테고리 필터링
            if (StringUtils.hasText(request.getCategory())) {
                predicates.add(criteriaBuilder.equal(root.get("category"), request.getCategory()));
            }
            
            // 상태 필터링
            if (StringUtils.hasText(request.getStatus())) {
                predicates.add(criteriaBuilder.equal(root.get("status"), request.getStatus()));
            }
            
            // 대여 가능 여부 필터링
            if (request.getAvailable() != null) {
                predicates.add(criteriaBuilder.equal(root.get("available"), request.getAvailable()));
            }
            
            // 검색어 필터링
            if (StringUtils.hasText(request.getSearchKeyword())) {
                String keyword = "%" + request.getSearchKeyword().toLowerCase() + "%";
                Integer searchType = request.getSearchType();
                
                if (searchType == null || searchType == 0) {
                    // 이름 검색
                    predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), keyword));
                } else if (searchType == 1) {
                    // 모델명 검색
                    predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("modelName")), keyword));
                } else {
                    // 기본적으로 이름과 모델명 모두 검색
                    Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), keyword);
                    Predicate modelPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("modelName")), keyword);
                    predicates.add(criteriaBuilder.or(namePredicate, modelPredicate));
                }
            }
            
            // 즐겨찾기 필터링
            if (request.getFavoriteOnly() != null && request.getFavoriteOnly()) {
                // 이 경우는 서비스 레이어에서 별도 처리 필요
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
    
    //장비 목록 필터링 유저용
    public static Specification<Equipment> filterEquipments2(EquipmentListRequest request, Integer userGrade) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            // 기본 필터링 조건은 관리자용과 동일
            // 카테고리 필터링
            if (StringUtils.hasText(request.getCategory())) {
                predicates.add(criteriaBuilder.equal(root.get("category"), request.getCategory()));
            }
            
            // 상태 필터링
            if (StringUtils.hasText(request.getStatus())) {
                predicates.add(criteriaBuilder.equal(root.get("status"), request.getStatus()));
            }
            
            // 대여 가능 여부 필터링
            if (request.getAvailable() != null) {
                predicates.add(criteriaBuilder.equal(root.get("available"), request.getAvailable()));
            }
            
            // 검색어 필터링
            if (StringUtils.hasText(request.getSearchKeyword())) {
                String keyword = "%" + request.getSearchKeyword().toLowerCase() + "%";
                Integer searchType = request.getSearchType();
                
                if (searchType == null || searchType == 0) {
                    // 이름 검색
                    predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), keyword));
                } else if (searchType == 1) {
                    // 모델명 검색
                    predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("modelName")), keyword));
                } else {
                    // 기본적으로 이름과 모델명 모두 검색
                    Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), keyword);
                    Predicate modelPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("modelName")), keyword);
                    predicates.add(criteriaBuilder.or(namePredicate, modelPredicate));
                }
            }
            
            //사용자 학년이 대여제한학년에 포함되지 않는 장비만 조회
            if (userGrade != null) {
                // rentalRestrictedGrades 필드가 null이거나 빈 문자열인 경우 포함
                Predicate noRestrictions = criteriaBuilder.or(
                    criteriaBuilder.isNull(root.get("rentalRestrictedGrades")),
                    criteriaBuilder.equal(root.get("rentalRestrictedGrades"), "")
                );
                
                // userGrade가 rentalRestrictedGrades에 포함되지 않는 경우
                Predicate notRestrictedForUser = criteriaBuilder.notLike(
                    root.get("rentalRestrictedGrades"), 
                    "%" + userGrade + "%"
                );
                
                // 두 조건을 or로 연결 (제한이 없거나, 사용자 학년이 제한에 포함되지 않음)
                predicates.add(criteriaBuilder.or(noRestrictions, notRestrictedForUser));
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
    //카테고리
    public static Specification<Equipment> hasCategory(String category) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(category)) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("category"), category);
        };
    }
    
    //상태
    public static Specification<Equipment> hasStatus(String status) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(status)) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }
    
    //대여가능여부
    public static Specification<Equipment> isAvailable(Boolean available) {
        return (root, query, criteriaBuilder) -> {
            if (available == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("available"), available);
        };
    }
    
    //이름 모델명 검색
    public static Specification<Equipment> containsKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(keyword)) {
                return criteriaBuilder.conjunction();
            }
            
            String likePattern = "%" + keyword.toLowerCase() + "%";
            
            Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), likePattern);
            Predicate modelPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("modelName")), likePattern);
            
            return criteriaBuilder.or(namePredicate, modelPredicate);
        };
    }
} 