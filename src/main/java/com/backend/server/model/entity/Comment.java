package com.backend.server.model.entity;

import com.backend.server.model.entity.enums.TargetType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.access.AccessDeniedException;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "comment")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TargetType type;

    @Column(nullable = false)
    private Long targetId;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> childComments = new ArrayList<>();

    public void validateAuthor(Long userId) {
        if (!this.author.getId().equals(userId)) {
            throw new AccessDeniedException("댓글 수정 권한이 없습니다.");
        }
    }
}
