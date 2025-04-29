package com.backend.server.api.common.dto.classRoom;

import java.util.List;

import org.springframework.data.domain.Page;

import com.backend.server.api.common.dto.PageableInfo;
import com.backend.server.model.entity.ClassRoom;

import lombok.Getter;

@Getter
public class CommonClassRoomListResponse {
    private List<CommonClassRoomResponse> content;
    private PageableInfo pageable;
    public CommonClassRoomListResponse(Page<ClassRoom> page) {
        this.content = page.getContent().stream().map(CommonClassRoomResponse::new).toList();
        this.pageable = new PageableInfo(page.getNumber(), page.getSize(), page.getTotalPages(), page.getTotalElements());
    }
    
}
