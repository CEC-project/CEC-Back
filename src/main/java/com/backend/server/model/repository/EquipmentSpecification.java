package com.backend.server.model.repository;

import com.backend.server.api.admin.dto.equipment.AdminEquipmentRentalRequestListRequest;
import com.backend.server.api.admin.dto.equipment.AdminEquipmentListRequest;
import com.backend.server.api.user.dto.equipment.EquipmentListRequest;
import com.backend.server.model.entity.Equipment;
import com.backend.server.model.entity.EquipmentCart;
import com.backend.server.model.entity.EquipmentFavorite;
import com.backend.server.model.entity.EquipmentRental;
import com.backend.server.model.entity.enums.RentalStatus;
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

    public static Pageable getPageable(
    Integer page, 
    Integer size, 
    String sortBy, 
    String sortDirection
) {
    // 정렬 기준 설정
    String sortField;
    if ("category".equals(sortBy)) {
        sortField = "category";
    } else if ("status".equals(sortBy)) {
        sortField = "status";
    } else {
        sortField = "name"; // 기본값은 이름순
    }

    // 정렬 방향 설정
    String direction = (sortDirection == null || !sortDirection.toLowerCase().equals("desc")) 
        ? "asc" 
        : "desc";

    // 페이지네이션 설정
    int pageNumber = page != null ? page - 1 : 0;  // 페이지는 0부터 시작
    int pageSize = size != null ? size : 17;       // 기본값 17

    return PageRequest.of(
        pageNumber,
        pageSize,
        Sort.by(Direction.fromString(direction), sortField)
    );
}
    
    // 페이징 및 정렬 정보 생성
    public static Pageable getPageable(EquipmentListRequest request) {
        String sortBy;
        if ("category".equals(request.getSortBy())) {
            sortBy = "category";
        } else if ("status".equals(request.getSortBy())) {
            sortBy = "status";
        } else {
            sortBy = "name"; // 기본값은 이름순
        }

        String sortDirection = request.getSortDirection();
        if (sortDirection == null || !sortDirection.toLowerCase().equals("desc")) {
            sortDirection = "asc";
        }

        int page = request.getPage() != null ? request.getPage() : 0;
        int size = request.getSize() != null ? request.getSize() : 17;

        return PageRequest.of(
                page,
                size,
                Sort.by(Direction.fromString(sortDirection), sortBy)
        );
    }
    //위와 완전 동일한 코드임. argument로 쓰는 DTO만 다름. 하지만 DTO의 내용도 동일함.
    public static Pageable getPageable(AdminEquipmentListRequest request) {
        String sortBy;
        if ("category".equals(request.getSortBy())) {
            sortBy = "category";
        } else if ("status".equals(request.getSortBy())) {
            sortBy = "status";
        } else {
            sortBy = "name"; // 기본값은 이름순
        }

        String sortDirection = request.getSortDirection();
        if (sortDirection == null || !sortDirection.toLowerCase().equals("desc")) {
            sortDirection = "asc";
        }

        int page = request.getPage() != null ? request.getPage() : 0;
        int size = request.getSize() != null ? request.getSize() : 17;

        return PageRequest.of(
                page,
                size,
                Sort.by(Direction.fromString(sortDirection), sortBy)
        );
    }
    
    // 장비 목록 필터링 (어드민용)
    public static Specification<Equipment> AdminfilterEquipments(AdminEquipmentListRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
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
            
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
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
            if (StringUtils.hasText(request.getStatus())) {
                predicates.add(criteriaBuilder.equal(root.get("status"), request.getStatus()));
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
                        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.join("equipment").get("name")), keyword));
                    } else if (searchType == 2) {
                        // 카테고리 검색
                        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.join("equipment").get("category")), keyword));
                    }
                } else {
                    // 기본적으로 모든 필드 검색
                    Predicate userNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.join("user").get("name")), keyword);
                    Predicate equipmentNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.join("equipment").get("name")), keyword);
                    Predicate categoryPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.join("equipment").get("category")), keyword);
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