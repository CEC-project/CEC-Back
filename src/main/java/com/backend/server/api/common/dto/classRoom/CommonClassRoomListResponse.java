package com.backend.server.api.common.dto.classRoom;

import java.util.List;

import org.springframework.data.domain.Page;

import com.backend.server.api.common.dto.PageableInfo;
import com.backend.server.model.entity.ClassRoom;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Getter;

@Getter
@Schema(description = "강의실 목록 응답 DTO")
public class CommonClassRoomListResponse {
    
    @Schema(description = "강의실 목록", example = "[{\"id\": 1, \"name\": \"101호\", \"capacity\": 30, \"createdAt\": \"2024-03-20T10:00:00\", \"updatedAt\": \"2024-03-20T10:00:00\"}]")
    private List<CommonClassRoomResponse> content;
    
    @Schema(description = "페이지네이션 정보")
    private PageableInfo pageable;
    
    public CommonClassRoomListResponse(Page<ClassRoom> page) {
        this.content = page.getContent().stream().map(CommonClassRoomResponse::new).toList();
        this.pageable = new PageableInfo(page.getNumber(), page.getSize(), page.getTotalPages(), page.getTotalElements());
    }
}
