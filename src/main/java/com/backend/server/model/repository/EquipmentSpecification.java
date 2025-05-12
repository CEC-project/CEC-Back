package com.backend.server.model.repository;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import com.backend.server.api.admin.equipment.dto.AdminEquipmentRentalRequestListRequest;
import com.backend.server.api.admin.equipment.dto.AdminEquipmentListRequest;
import com.backend.server.api.user.equipment.dto.EquipmentListRequest;
import com.backend.server.model.entity.Equipment;
import com.backend.server.model.entity.EquipmentCart;
import com.backend.server.model.entity.EquipmentFavorite;
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
                predicates.add(cb.like(cb.lower(renter.get("name")), "%" + request.getRenterName().toLowerCase() + "%"));
            }
            // 통합 검색어 (모델명, 일련번호, 대여자 이름 등)
            if (StringUtils.hasText(request.getSearchKeyword())) {
                String keyword = "%" + request.getSearchKeyword().toLowerCase() + "%";
                Predicate modelNamePredicate = cb.like(cb.lower(root.get("modelName")), keyword);
                Predicate serialNumberPredicate = cb.like(cb.lower(root.get("serialNumber")), keyword);
                Join<Equipment, User> renter = root.join("renter", JoinType.LEFT);
                Predicate renterNamePredicate = cb.like(cb.lower(renter.get("name")), keyword);
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
    
    //장비 목록 필터링 유저용
    public static Specification<Equipment> filterEquipments(EquipmentListRequest request, Integer userGrade) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            // 기본 필터링 조건은 관리자용과 동일
            // 카테고리 필터링
            if (request.getCategoryId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("categoryId"), request.getCategoryId()));
            }
            
            // 상태 필터링
            if (request.getRentalStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("rentalStatus"), request.getRentalStatus()));
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
            
            // 장바구니 필터링
            if (request.getInCart() != null && request.getInCart() && request.getUserId() != null) {
                // 장바구니에 있는 장비만 필터링
                Subquery<Long> cartSubquery = query.subquery(Long.class);
                Root<EquipmentCart> cartRoot = cartSubquery.from(EquipmentCart.class);
                cartSubquery.select(cartRoot.get("equipmentId"))
                    .where(
                        criteriaBuilder.equal(cartRoot.get("userId"), request.getUserId())
                    );
                
                // 메인 쿼리에 서브쿼리 조건 추가
                predicates.add(criteriaBuilder.in(root.get("id")).value(cartSubquery));
            }

            // 즐겨찾기 필터링
            if (request.getIsFavorite() != null && request.getIsFavorite() && request.getUserId() != null) {
                // 즐겨찾기에 있는 장비만 필터링
                Subquery<Long> favoriteSubquery = query.subquery(Long.class);
                Root<EquipmentFavorite> favoriteRoot = favoriteSubquery.from(EquipmentFavorite.class);
                favoriteSubquery.select(favoriteRoot.get("equipmentId"))
                    .where(
                        criteriaBuilder.equal(favoriteRoot.get("userId"), request.getUserId())
                    );
                
                // 메인 쿼리에 서브쿼리 조건 추가
                predicates.add(criteriaBuilder.in(root.get("id")).value(favoriteSubquery));
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
    
    // 대여 요청 목록 필터링 (어드민용)
    public static Specification<EquipmentRental> filterRentalRequests(AdminEquipmentRentalRequestListRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            // 대여 상태 필터링
            if (request.getRentalStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("rentalStatus"), request.getRentalStatus()));
            }
            
            // 검색어 필터링 (장비 이름, 모델명)
            if (StringUtils.hasText(request.getSearchKeyword())) {
                String keyword = "%" + request.getSearchKeyword().toLowerCase() + "%";
                Integer searchType = request.getSearchType();
                
                if (searchType != null) {
                    if (searchType == 0) {
                        // 사용자 이름 검색
                        Subquery<Long> userSubquery = query.subquery(Long.class);
                        Root<User> userRoot = userSubquery.from(User.class);
                        userSubquery.select(userRoot.get("id"))
                            .where(criteriaBuilder.like(criteriaBuilder.lower(userRoot.get("name")), keyword));
                        predicates.add(criteriaBuilder.in(root.get("userId")).value(userSubquery));
                    } else if (searchType == 1) {
                        // 장비 이름 검색
                        Subquery<Long> equipmentSubquery = query.subquery(Long.class);
                        Root<Equipment> equipmentRoot = equipmentSubquery.from(Equipment.class);
                        equipmentSubquery.select(equipmentRoot.get("id"))
                            .where(criteriaBuilder.like(criteriaBuilder.lower(equipmentRoot.get("name")), keyword));
                        predicates.add(criteriaBuilder.in(root.get("equipmentId")).value(equipmentSubquery));
                    } else if (searchType == 2) {
                        // 카테고리 검색
                        Subquery<Long> equipmentSubquery = query.subquery(Long.class);
                        Root<Equipment> equipmentRoot = equipmentSubquery.from(Equipment.class);
                        equipmentSubquery.select(equipmentRoot.get("id"))
                            .where(criteriaBuilder.like(criteriaBuilder.lower(equipmentRoot.get("categoryId").as(String.class)), keyword));
                        predicates.add(criteriaBuilder.in(root.get("equipmentId")).value(equipmentSubquery));
                    }
                } else {
                    // 기본적으로 모든 필드 검색
                    Subquery<Long> userSubquery = query.subquery(Long.class);
                    Root<User> userRoot = userSubquery.from(User.class);
                    userSubquery.select(userRoot.get("id"))
                        .where(criteriaBuilder.like(criteriaBuilder.lower(userRoot.get("name")), keyword));
                    
                    Subquery<Long> equipmentSubquery = query.subquery(Long.class);
                    Root<Equipment> equipmentRoot = equipmentSubquery.from(Equipment.class);
                    equipmentSubquery.select(equipmentRoot.get("id"))
                        .where(criteriaBuilder.or(
                            criteriaBuilder.like(criteriaBuilder.lower(equipmentRoot.get("name")), keyword),
                            criteriaBuilder.like(criteriaBuilder.lower(equipmentRoot.get("categoryId").as(String.class)), keyword)
                        ));
                    
                    predicates.add(criteriaBuilder.or(
                        criteriaBuilder.in(root.get("userId")).value(userSubquery),
                        criteriaBuilder.in(root.get("equipmentId")).value(equipmentSubquery)
                    ));
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
    
    // 페이징 및 정렬 정보 생성 (대여 요청 목록용)
    public static Pageable getRentalRequestPageable(AdminEquipmentRentalRequestListRequest request) {
        String sortBy = "createdAt"; // 기본값은 생성일자순
        
        if (StringUtils.hasText(request.getSortBy())) {
            if (request.getSortBy().contains("status")) {
                sortBy = "status";
            } else if (request.getSortBy().contains("rentalTime")) {
                sortBy = "rentalTime";
            } else if (request.getSortBy().contains("returnTime")) {
                sortBy = "returnTime";
            }
        }
        
        Direction direction = Direction.DESC; // 기본값은 내림차순
        
        if (StringUtils.hasText(request.getSortBy()) && request.getSortBy().contains("asc")) {
            direction = Direction.ASC;
        }
        
        int page = request.getPage() != null ? request.getPage() : 0;
        int size = request.getSize() != null ? request.getSize() : 10;
        
        return PageRequest.of(page, size, Sort.by(direction, sortBy));
    }

    
} 