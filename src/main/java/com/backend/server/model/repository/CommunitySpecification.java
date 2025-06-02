package com.backend.server.model.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.backend.server.api.admin.notice.dto.AdminNoticeListRequest;
import com.backend.server.model.entity.Community;

import jakarta.persistence.criteria.Predicate;

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
}
