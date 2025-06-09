package com.backend.server.model.repository;

import com.backend.server.api.admin.community.dto.AdminCommunityListRequest;
import com.backend.server.api.admin.community.dto.CommunityListRequest;
import com.backend.server.api.admin.notice.dto.AdminNoticeListRequest;
import com.backend.server.model.entity.Community;
import com.backend.server.model.entity.Notice;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class CommunitySpecification {
    public static Specification<Community> filterCommunity(AdminNoticeListRequest request) {
        return ((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(request.getSearchKeyword())) {
                String[] keywords = request.getSearchKeyword().trim().split("\\s+");

                switch (request.getSearchType()) {
                    case TITLE -> {
                        for (String keyword : keywords) {
                            predicates.add(cb.like(cb.lower(root.get("title")), "%" + keyword.toLowerCase() + "%"));
                        }
                    }
                    case CONTENT -> {
                        for (String keyword : keywords) {
                            predicates.add(cb.like(cb.lower(root.get("content")), "%" + keyword.toLowerCase() + "%"));
                        }
                    }
                    case ALL -> {
                        for (String keyword : keywords) {
                            Predicate titlePredicate = cb.like(cb.lower(root.get("title")), "%" + keyword.toLowerCase() + "%");
                            Predicate contentPredicate = cb.like(cb.lower(root.get("content")), "%" + keyword.toLowerCase() + "%");
                            predicates.add(cb.or(titlePredicate, contentPredicate));
                        }
                    }
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        });
    }

    public static Specification<Community> filterCommunities(AdminCommunityListRequest request) {
        return (root, query, cb) -> {
            var predicate = cb.conjunction();

            predicate = searchAndFilterCommunities(root, cb, predicate, request);

            return predicate;
        };
    }

    public static Predicate searchAndFilterCommunities(
            From<?, Community> root, CriteriaBuilder cb, Predicate predicate, AdminCommunityListRequest request) {

        // 이름, 제목, 내용, 닉네임 으로 검색합니다.
        String keyword = "%" + request.getSearchKeyword() + "%";
        if (request.getSearchKeyword() == null || request.getSearchKeyword().isEmpty())
            keyword = "%";

        Join<Object, Object> author = root.join("author", JoinType.LEFT);
        Predicate name = cb.like(author.get("name"), keyword);
        Predicate title = cb.like(root.get("title"), keyword);
        Predicate content = cb.like(root.get("content"), keyword);
        Predicate nickname = cb.like(root.get("nickname"), keyword);
        Predicate all = cb.or(name, title, content, nickname);

        switch (request.getSearchType()) {
            case NAME -> predicate = cb.and(predicate, name);
            case TITLE -> predicate = cb.and(predicate, title);
            case CONTENT -> predicate = cb.and(predicate, content);
            case NICKNAME -> predicate = cb.and(predicate, nickname);
            case ALL -> predicate = cb.and(predicate, all);
        }

        return predicate;
    }

    //--------------------------
    public static Specification<Community> filterCommunitiesForUser(CommunityListRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(request.getSearchKeyword())) {
                String[] keywords = request.getSearchKeyword().trim().toLowerCase().split("\\s+");
                for (String kw : keywords) {
                    String pattern = "%" + kw + "%";

                    switch (request.getSearchType()) {
                        case TITLE:
                            predicates.add(cb.like(cb.lower(root.get("title")), pattern));
                            break;

                        case CONTENT:
                            predicates.add(cb.like(cb.lower(root.get("content")), pattern));
                            break;

                        case NAME:
                            predicates.add(cb.like(cb.lower(root.get("author").get("name")), pattern));
                            break;

                        case NICKNAME:
                            predicates.add(cb.like(cb.lower(root.get("author").get("nickname")), pattern));
                            break;

                        case ALL:
                        default:
                            Predicate titlePred    = cb.like(cb.lower(root.get("title")), pattern);
                            Predicate contentPred  = cb.like(cb.lower(root.get("content")), pattern);
                            Predicate namePred     = cb.like(cb.lower(root.get("author").get("name")), pattern);
                            Predicate nicknamePred = cb.like(cb.lower(root.get("author").get("nickname")), pattern);
                            predicates.add(cb.or(titlePred, contentPred, namePred, nicknamePred));
                            break;
                    }
                }
            }

            // 2) 카테고리 필터
            if (request.getCategoryId() != null) {
                predicates.add(cb.equal(root.get("boardCategory").get("id"), request.getCategoryId()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
