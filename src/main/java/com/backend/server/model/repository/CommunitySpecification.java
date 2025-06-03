package com.backend.server.model.repository;

import com.backend.server.api.admin.community.dto.AdminCommunityListRequest;
import com.backend.server.api.admin.notice.dto.AdminNoticeListRequest;
import com.backend.server.model.entity.Community;
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

                switch (request.getAdminNoticeSearchType()) {
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
}
