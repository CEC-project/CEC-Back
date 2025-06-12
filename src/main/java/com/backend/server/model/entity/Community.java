package com.backend.server.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Community extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String nickname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User author;

    @Column
    private int recommend;

    @Column
    private int view;

//    @Column
//    private String type;
//
//    @Column
//    private Long typeId;
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private CommunityType communityType;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "attachment_url")
    private String attachmentUrl;  // 첨부파일 URL

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_category_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private BoardCategory boardCategory;

    public void increaseViewCount() {
        this.view++;
    }
}
