package com.backend.server.model.repository;

import com.backend.server.model.entity.Comment;
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
            """)
    Page<Comment> findAllByTargetIdAndParentCommentIsNullWithAuthor(Long targetId, Pageable pageable);

    @Query("""
            SELECT c
            FROM Comment c
                JOIN FETCH c.author
            WHERE c.parentComment.id IN :parentIds
            """)
    List<Comment> findAllByParentCommentIdInWithAuthor(List<Long> parentIds);
}
