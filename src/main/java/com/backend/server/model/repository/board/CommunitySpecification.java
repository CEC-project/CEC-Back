package com.backend.server.model.repository.board;

import com.backend.server.api.admin.community.dto.AdminCommunityListRequest;
import com.backend.server.api.user.board.dto.PostListRequest;
import com.backend.server.model.entity.Board;
import com.backend.server.model.entity.BoardCategory;
import com.backend.server.model.entity.User;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class CommunitySpecification {
    public static Specification<Board> filterCommunities(AdminCommunityListRequest request) {
        return (root, query, cb) -> {
            var predicate = cb.conjunction();

            predicate = searchAndFilterCommunities(root, cb, predicate, request);

            return predicate;
        };
    }

    public static Predicate searchAndFilterCommunities(
            From<?, Board> root, CriteriaBuilder cb, Predicate predicate, AdminCommunityListRequest request) {

        // 이름, 제목, 내용, 닉네임 으로 검색합니다.
        String keyword = "%" + request.getSearchKeyword() + "%";
        if (request.getSearchKeyword() == null || request.getSearchKeyword().isEmpty())
            keyword = "%";

        Predicate title = cb.like(root.get("title"), keyword);
        Predicate content = cb.like(root.get("content"), keyword);

        Join<Board, User> author = root.join("author", JoinType.LEFT);
        Predicate name = cb.like(author.get("name"), keyword);
        Predicate nickname = cb.like(author.get("nickname"), keyword);

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
    public static Specification<Board> filterCommunitiesForUser(PostListRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            Join<Board, User> author = root.join("author", JoinType.LEFT);
            Join<Board, BoardCategory> category = root.join("boardCategory", JoinType.LEFT);

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
                            predicates.add(cb.like(cb.lower(author.get("name")), pattern));
                            break;

                        case NICKNAME:
                            predicates.add(cb.like(cb.lower(author.get("nickname")), pattern));
                            break;

                        case ALL:
                        default:
                            Predicate titlePred    = cb.like(cb.lower(root.get("title")), pattern);
                            Predicate contentPred  = cb.like(cb.lower(root.get("content")), pattern);
                            Predicate namePred     = cb.like(cb.lower(author.get("name")), pattern);
                            Predicate nicknamePred = cb.like(cb.lower(author.get("nickname")), pattern);
                            predicates.add(cb.or(titlePred, contentPred, namePred, nicknamePred));
                            break;
                    }
                }
            }

            // 2) 카테고리 필터
            if (request.getCategoryId() != null)
                predicates.add(cb.equal(category.get("id"), request.getCategoryId()));

            // 3) 작성자 필터
            if (request.getAuthorId() != null)
                predicates.add(cb.equal(author.get("id"), request.getAuthorId()));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
