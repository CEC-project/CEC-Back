package com.backend.server.api.admin.notice.dto;

import com.backend.server.api.common.dto.AdminAuthorResponse;
import com.backend.server.model.entity.Notice;
import com.backend.server.model.entity.User;
import io.micrometer.common.util.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;

@Getter
public class AdminNoticeResponse {

    @Schema(description = "공지사항 ID", example = "1")
    private Long id;

    @Schema(description = "공지사항 제목", example = "시스템 점검 안내")
    private String title;

    @Schema(description = "공지사항 본문 내용", example = "시스템 점검이 6월 3일 00시에 진행됩니다.")
    private String content;

    // important 로 통일하려고 합니다. -qkr10
    @Schema(description = "중요 공지 여부 (true이면 상단 고정)", example = "true")
    private Boolean important;

    @Schema(description = "첨부 파일 URL", nullable = true)
    private List<String> attachments;

    @Schema(description = "공지사항 조회수", example = "132")
    private Integer view;

    @Schema(description = "공지 등록 시간", example = "2025-06-01T12:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "공지 수정 시간", example = "2025-06-01T14:30:00")
    private LocalDateTime updatedAt;

    @Schema(description = "공지 작성자 정보")
    private AdminAuthorResponse author;

    public AdminNoticeResponse(Notice notice) {
        User user = notice.getAuthor();

        List<String> attachments = new ArrayList<>();
        if (!StringUtils.isEmpty(notice.getAttachmentUrl()))
            attachments = Arrays.stream(notice.getAttachmentUrl().split(";")).toList();
        this.attachments = attachments;

        this.id = notice.getId();
        this.title = notice.getTitle();
        this.content = notice.getContent();
        this.important = notice.getImportant();
        this.view = notice.getView();
        this.createdAt = notice.getCreatedAt();
        this.updatedAt = notice.getUpdatedAt();

        this.author = user == null ? null : AdminAuthorResponse.from(user);
    }
}
