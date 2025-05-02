package com.backend.server.api.user.dto.classroom;

import java.util.List;
import org.springframework.data.domain.Page;
import com.backend.server.api.common.dto.PageableInfo;
import com.backend.server.model.entity.ClassRoom;
import lombok.Getter;

@Getter
public class ClassRoomListResponse {
    private List<ClassRoomResponse> content;
    private PageableInfo pageable;

    public ClassRoomListResponse(Page<ClassRoom> page) {
        this.content = page.getContent().stream().map(ClassRoomResponse::new).toList();
        this.pageable = new PageableInfo(page.getNumber(), page.getSize(), page.getTotalPages(), page.getTotalElements());
    }
} 