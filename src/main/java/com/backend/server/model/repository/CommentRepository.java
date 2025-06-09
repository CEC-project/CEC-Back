package com.backend.server.model.repository;

import com.backend.server.model.entity.Comment;
import com.backend.server.model.entity.enums.TargetType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("""
            SELECT c
            FROM Comment c
                JOIN FETCH c.author
            WHERE c.targetId = :targetId
                AND c.parentComment IS NULL
                AND c.type = :type
            """)
    Page<Comment> findAllByTypeAndTargetIdAndParentCommentIsNullWithAuthor(TargetType type, Long targetId, Pageable pageable);

    @Query("""
            SELECT c
            FROM Comment c
                JOIN FETCH c.author
            WHERE c.parentComment.id IN :parentIds
            """)
    List<Comment> findAllByParentCommentIdInWithAuthor(List<Long> parentIds);
}
